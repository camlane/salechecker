package org.icognition.salechecker.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.icognition.salechecker.domain.SiteDocument
import org.icognition.salechecker.entity.Product
import org.icognition.salechecker.entity.ScanResult
import org.icognition.salechecker.entity.ScanResult.ScanStatus.ELEMENT_FOUND
import org.icognition.salechecker.entity.Site
import org.icognition.salechecker.entity.SiteItem
import org.icognition.salechecker.http.ReactiveHttpClient
import org.icognition.salechecker.mail.EmailMessageBuilder
import org.icognition.salechecker.mail.EmailService
import org.icognition.salechecker.repository.SiteItemRepository
import org.icognition.salechecker.scan.JsoupSiteScanner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus.OK
import reactor.core.publisher.Flux
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN

@ExperimentalCoroutinesApi
@FlowPreview
@ExtendWith(MockKExtension::class)
internal class SiteItemCheckServiceTest {

  @MockK
  lateinit var siteItemRepository: SiteItemRepository
  @MockK
  lateinit var siteScanner: JsoupSiteScanner
  @MockK
  lateinit var httpClient: ReactiveHttpClient
  @RelaxedMockK
  lateinit var emailService: EmailService
  @MockK
  lateinit var emailMessageBuilder: EmailMessageBuilder

  @InjectMockKs
  lateinit var siteCheckService: SiteCheckService

  @BeforeEach
  fun setUp() {
    every { siteItemRepository.findAll() } returns SITES
    coEvery { httpClient.get(URL) } returns SITE_HTML
  }

  @Test
  fun `should call findAll`() {

    runBlocking {
      siteCheckService.checkSites()
    }

    verify { siteItemRepository.findAll() }
  }

  @Test
  fun `should call get on httpClient`() {
    runBlocking {
      siteCheckService.checkSites()
    }
    coVerify { httpClient.get(URL) }
  }

  @Test
  fun `should call scanSite`() {
    runBlocking {
      siteCheckService.checkSites()
    }
    verify { siteScanner.scanSite(SiteDocument(SITE_ITEM, SITE_HTML)) }
  }

  @Test
  fun `should call generateMailMessage when price has decreased`() {
    val scanResult = ScanResult(ONE, OK, ELEMENT_FOUND, SITE_ITEM)
    coEvery {
      siteScanner.scanSite(SiteDocument(SITE_ITEM, SITE_HTML))
    } returns scanResult

    runBlocking {
      siteCheckService.checkSites()
    }
    verify {
      emailMessageBuilder.generateMailMessage(scanResult)
    }
  }

  @Test
  fun `should call sendMail when element found and price has decreased`() {
    val scanResult = ScanResult(ONE, OK, ELEMENT_FOUND, SITE_ITEM)
    coEvery {
      siteScanner.scanSite(SiteDocument(SITE_ITEM, SITE_HTML))
    } returns scanResult

    coEvery {
      emailMessageBuilder.generateMailMessage(scanResult)
    } returns "html"

    runBlocking {
      siteCheckService.checkSites()
    }
    verify {
      emailService.sendSimpleMessage("cam.lane@gmail.com", "Price change alert", "html")
    }
  }

  companion object {

    private const val URL = "http://test.com?pid=1"
    private const val SITE_HTML = "<html><body>test</body></html>"

    private val PRODUCT = Product("test")
    private val SITE = Site("test", "css")
    private val SITE_ITEM = SiteItem(URL, TEN, PRODUCT, SITE)
    private val SITES = Flux.just(SITE_ITEM)
  }

}
