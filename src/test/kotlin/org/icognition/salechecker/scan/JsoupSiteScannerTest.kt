package org.icognition.salechecker.scan

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.icognition.salechecker.domain.SiteDocument
import org.icognition.salechecker.entity.Product
import org.icognition.salechecker.entity.ScanResult.ScanStatus.*
import org.icognition.salechecker.entity.Site
import org.icognition.salechecker.entity.SiteItem
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.math.BigDecimal.ONE

@ExtendWith(MockKExtension::class)
class JsoupSiteScannerTest {

  @MockK
  lateinit var priceParser: PriceParser

  @InjectMockKs
  lateinit var siteScanner: JsoupSiteScanner

  @Test
  fun `when selector does not find a match then scan status is ElementNotFound`() {

    val siteDocument = getSiteDocument("body > xxx", getHtml("100.00"))

    val result = siteScanner.scanSite(siteDocument)
    result.scanStatus.shouldBe(ElementNotFound)
  }

  @Test
  fun `when price element found then scan status is ElementNotFound`() {

    every { priceParser.parse("100.00") } returns BigDecimal("100.00")

    val siteDocument = getSiteDocument("body > price", getHtml("100.00"))

    val result = siteScanner.scanSite(siteDocument)
    result.scanStatus.shouldBe(ElementFound)
  }

  @Test
  fun `when price element is found then price matches`() {

    every { priceParser.parse("123.45") } returns BigDecimal("123.45")

    val siteDocument = getSiteDocument("body > price", getHtml("123.45"))

    val result = siteScanner.scanSite(siteDocument)
    result.scanPrice.shouldBe(BigDecimal("123.45"))
  }

  @Test
  fun `when price element contains pound symbol then scan price is correct`() {

    every { priceParser.parse("£123.45") } returns BigDecimal("123.45")

    val siteDocument = getSiteDocument("body > price", getHtml("£123.45"))

    val result = siteScanner.scanSite(siteDocument)
    result.scanPrice.shouldBe(BigDecimal("123.45"))
  }

  @Test
  fun `when price element contains currency then scan price is correct`() {

    val siteDocument = getSiteDocument("body > price", getHtml("GBP 123.45"))

    every { priceParser.parse("GBP 123.45") } returns BigDecimal("123.45")

    val result = siteScanner.scanSite(siteDocument)
    result.scanPrice.shouldBe(BigDecimal("123.45"))
  }

  @Test
  fun `when price element is empty then scan status is ElementNotFound`() {

    val siteDocument = getSiteDocument("body > price", getHtml(""))

    val result = siteScanner.scanSite(siteDocument)
    result.scanStatus.shouldBe(ElementNotFound)
  }

  @Test
  fun `when element price is not valid then scan status is InvalidPrice`() {

    every {
      priceParser.parse("XXX")
    } throws InvalidPriceTextException(NumberFormatException("Error parsing price"))

    val siteDocument = getSiteDocument("body > price", getHtml("XXX"))

    val result = siteScanner.scanSite(siteDocument)
    result.scanStatus.shouldBe(InvalidPrice)
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