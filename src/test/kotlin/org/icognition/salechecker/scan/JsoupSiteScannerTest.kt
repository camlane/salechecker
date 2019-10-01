package org.icognition.salechecker.scan

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.icognition.salechecker.domain.SiteDocument
import org.icognition.salechecker.entity.Product
import org.icognition.salechecker.entity.ScanResult
import org.icognition.salechecker.entity.ScanResult.ScanStatus
import org.icognition.salechecker.entity.ScanResult.ScanStatus.*
import org.icognition.salechecker.entity.Site
import org.icognition.salechecker.entity.SiteItem
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.text.ParseException

@ExtendWith(MockKExtension::class)
class JsoupSiteScannerTest {

  @MockK
  lateinit var priceParser: PriceParser

  @InjectMockKs
  lateinit var siteScanner: JsoupSiteScanner

  @Test
  fun `when selector does not find a match then scan status is ELEMENT_NOT_FOUND`() {

    val siteDocument = getSiteDocument("body > xxx", getHtml("100.00"))

    val result = siteScanner.scanSite(siteDocument)
    assertScanStatus(result, ELEMENT_NOT_FOUND)
  }

  @Test
  fun `when price element found then scan status is ELEMENT_FOUND`() {

    every { priceParser.parse("100.00") } returns BigDecimal("100.00")

    val siteDocument = getSiteDocument("body > price", getHtml("100.00"))

    val result = siteScanner.scanSite(siteDocument)
    assertScanStatus(result, ELEMENT_FOUND)
  }

  @Test
  fun `when price element is found then price matches`() {

    every { priceParser.parse("123.45") } returns BigDecimal("123.45")

    val siteDocument = getSiteDocument("body > price", getHtml("123.45"))

    val result = siteScanner.scanSite(siteDocument)
    assertThat(result.scanPrice, `is`(BigDecimal("123.45")))
  }

  @Test
  fun `when price element contains pound symbol then scan price is correct`() {

    every { priceParser.parse("£123.45") } returns BigDecimal("123.45")

    val siteDocument = getSiteDocument("body > price", getHtml("£123.45"))

    val result = siteScanner.scanSite(siteDocument)
    assertThat(result.scanPrice, `is`(BigDecimal("123.45")))
  }

  @Test
  fun `when price element contains currency then scan price is correct`() {

    val siteDocument = getSiteDocument("body > price", getHtml("GBP 123.45"))

    every { priceParser.parse("GBP 123.45") } returns BigDecimal("123.45")

    val result = siteScanner.scanSite(siteDocument)
    assertThat(result.scanPrice, `is`(BigDecimal("123.45")))
  }

  @Test
  fun `when price element is empty then scan status is ELEMENT_NOT_FOUND`() {

    val siteDocument = getSiteDocument("body > price", getHtml(""))

    val result = siteScanner.scanSite(siteDocument)
    assertScanStatus(result, ELEMENT_NOT_FOUND)
  }

  @Test
  fun `when element price is not valid then scan status is INVALID_PRICE`() {

    every { priceParser.parse("XXX") } throws ParseException("", 1)

    val siteDocument = getSiteDocument("body > price", getHtml("XXX"))

    val result = siteScanner.scanSite(siteDocument)
    assertScanStatus(result, INVALID_PRICE)
  }

  private fun assertScanStatus(result: ScanResult, scanStatus: ScanStatus) {
    assertThat(result.scanStatus, `is`<ScanStatus>(scanStatus))
  }

  private fun getHtml(price: String): String = "<html><body>" +
      "<price product_id=\"123456\">$price</price>" +
      "</body></html>"

  private fun getSiteDocument(cssSelector: String, siteHtml: String): SiteDocument {
    val product = Product("Nike Air Max")
    val site = Site("ASOS", cssSelector)
    val siteItem = SiteItem("asos.com?pid=1", ONE, product, site)
    return SiteDocument(siteItem, siteHtml)
  }

}