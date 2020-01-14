package org.icognition.salechecker.service

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import mu.KLogging
import org.icognition.salechecker.domain.SiteDocument
import org.icognition.salechecker.entity.ScanResult
import org.icognition.salechecker.entity.ScanResult.ScanStatus.ElementFound
import org.icognition.salechecker.entity.SiteItem
import org.icognition.salechecker.http.ReactiveHttpClient
import org.icognition.salechecker.mail.EmailMessageBuilder
import org.icognition.salechecker.mail.EmailService
import org.icognition.salechecker.repository.ScanResultRepository
import org.icognition.salechecker.repository.SiteItemRepository
import org.icognition.salechecker.repository.findAllAsFlow
import org.icognition.salechecker.scan.JsoupSiteScanner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@ExperimentalCoroutinesApi
@FlowPreview
@Service
class SiteCheckService {

    companion object : KLogging()

    @Autowired
    lateinit var siteItemRepository: SiteItemRepository
    @Autowired
    lateinit var scanResultRepository: ScanResultRepository
    @Autowired
    lateinit var siteScanner: JsoupSiteScanner
    @Autowired
    lateinit var httpClient: ReactiveHttpClient
    @Autowired
    lateinit var emailService: EmailService
    @Autowired
    lateinit var emailMessageBuilder: EmailMessageBuilder

    //  @Scheduled(cron = "0 09 16 * * *")
    suspend fun checkSites() {

        val siteItems = siteItemRepository.findAllAsFlow()

        val responses = siteItems
            .map { getResponse(it) }

        val successfulResponses =
            responses
                .filterNot { it.second.statusCode.isError }

        responses
            .filter { it.second.statusCode.isError }
            .onEach {
                val scanResult = toScanResult(it)
                scanResultRepository.save(scanResult)
            }

        successfulResponses
            .map { toSiteDocument(it) }
            .map { siteScanner.scanSite(it) }
            .filter { it.priceFound() || it.priceHasDecreased() }
            .map {
                emailMessageBuilder.generateMailMessage(it)
            }
            .catch { logError(it) }
            .collect { sendMail(it) }
    }

    private fun toScanResult(siteItemAndResponse: Pair<SiteItem, ResponseEntity<String>>): ScanResult =
        ScanResult(
            httpStatus = siteItemAndResponse.second.statusCode,
            siteItem = siteItemAndResponse.first
        )

    private fun logError(e: Throwable?) {
        logger.error(e) { "Error checking sites" }
    }

    private fun ScanResult.priceFound(): Boolean {
        this.let {
            logger.info { "Scan status for $this.siteItem.url - $this.scanStatus" }
            logger.info { "Selector: $this.siteItem.site.cssSelector" }
        }

        return this.scanStatus == ElementFound
    }

    private fun ScanResult.priceHasDecreased(): Boolean =
        this.scanPrice!! < this.siteItem.originalPrice

    private fun sendMail(messageBody: String) {
        val subject = "Price change alert"
        emailService.sendSimpleMessage("cam.lane@gmail.com", subject, messageBody)
    }

    private suspend fun toSiteDocument(siteItemAndResponse: SiteItemAndResponse):
        SiteDocument = SiteDocument(
        siteItemAndResponse.first,
        siteItemAndResponse.second.body!!.toString()
    )

    private suspend fun getResponse(siteItem: SiteItem): SiteItemAndResponse =
        Pair(siteItem, httpClient.get(siteItem.url))
}

typealias SiteItemAndResponse = Pair<SiteItem, ResponseEntity<String>>
