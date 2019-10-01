package org.icognition.salechecker.service

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import mu.KLogging
import org.icognition.salechecker.domain.SiteDocument
import org.icognition.salechecker.entity.ScanResult
import org.icognition.salechecker.entity.ScanResult.ScanStatus.ELEMENT_FOUND
import org.icognition.salechecker.entity.SiteItem
import org.icognition.salechecker.http.ReactiveHttpClient
import org.icognition.salechecker.mail.EmailMessageBuilder
import org.icognition.salechecker.mail.EmailService
import org.icognition.salechecker.repository.SiteItemRepository
import org.icognition.salechecker.repository.findAllAsFlow
import org.icognition.salechecker.scan.JsoupSiteScanner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@ExperimentalCoroutinesApi
@FlowPreview
@Service
class SiteCheckService {

  companion object : KLogging()

  @Autowired
  lateinit var siteItemRepository: SiteItemRepository
  @Autowired
  lateinit var siteScanner: JsoupSiteScanner
  @Autowired
  lateinit var httpClient: ReactiveHttpClient
  @Autowired
  lateinit var emailService: EmailService
  @Autowired
  lateinit var emailMessageBuilder: EmailMessageBuilder

  @FlowPreview
//  @Scheduled(cron = "0 09 16 * * *")
  suspend fun checkSites() {

    val siteItems = siteItemRepository.findAllAsFlow()

    siteItems
        .map { getSiteDocument(it) }
        .map { siteScanner.scanSite(it) }
        .filter { it.priceFound() }
        .filter { it.priceHasDecreased() }
        .map {
          emailMessageBuilder.generateMailMessage(it)
        }
        .catch { logError(it) }
        .collect { sendMail(it) }
  }

  private fun logError(e: Throwable?) {
    logger.error(e) { "Error checking sites" }
  }

  private fun ScanResult.priceFound(): Boolean {
    this.let {
      logger.info { "Scan status for $this.siteItem.url - $this.scanStatus" }
      logger.info { "Selector: $this.siteItem.site.cssSelector" }
    }

    return this.scanStatus == ELEMENT_FOUND
  }

  private fun ScanResult.priceHasDecreased(): Boolean {
    return this.scanPrice!! < this.siteItem.originalPrice
  }

  private fun sendMail(messageBody: String) {
    val subject = "Price change alert"
    emailService.sendSimpleMessage("cam.lane@gmail.com", subject, messageBody)
  }

  private suspend fun getSiteDocument(siteItem: SiteItem): SiteDocument {
    val siteHtml = httpClient.get(siteItem.url)
    return SiteDocument(siteItem, siteHtml)
  }

}
