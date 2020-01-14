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
    fun `scan status is ElementNotFound when selector does not find a match`() {

        val siteDocument = getSiteDocument(getHtml("100.00"), "body > xxx")

        val result = siteScanner.scanSite(siteDocument)
        result.scanStatus.shouldBe(ElementNotFound)
    }

    @Test
    fun `scan status is ElementFound when price element found`() {

        every { priceParser.parse("100.00") } returns BigDecimal("100.00")

        val siteDocument = getSiteDocument(getHtml("100.00"))
        val result = siteScanner.scanSite(siteDocument)
        result.scanStatus.shouldBe(ElementFound)
    }

    @Test
    fun `prices is correct when price element found`() {

        every { priceParser.parse("123.45") } returns BigDecimal("123.45")

        val siteDocument = getSiteDocument(getHtml("123.45"))
        val result = siteScanner.scanSite(siteDocument)
        result.scanPrice.shouldBe(BigDecimal("123.45"))
    }

    @Test
    fun `prices is correct when price element contains pound symbol`() {

        every { priceParser.parse("£123.45") } returns BigDecimal("123.45")

        val siteDocument = getSiteDocument(getHtml("£123.45"))
        val result = siteScanner.scanSite(siteDocument)
        result.scanPrice.shouldBe(BigDecimal("123.45"))
    }

    @Test
    fun `price is correct when price element contains currency`() {

        val siteDocument = getSiteDocument(getHtml("GBP 123.45"))
        every { priceParser.parse("GBP 123.45") } returns BigDecimal("123.45")

        val result = siteScanner.scanSite(siteDocument)
        result.scanPrice.shouldBe(BigDecimal("123.45"))
    }

    @Test
    fun `scan status is ElementNotFound when price element is empty`() {

        val siteDocument = getSiteDocument(getHtml(""))
        val result = siteScanner.scanSite(siteDocument)
        result.scanStatus.shouldBe(ElementNotFound)
    }

    @Test
    fun `scan status is InvalidPrice when price element is not valid`() {

        every {
            priceParser.parse("XXX")
        } throws NumberFormatException("Error parsing price")

        val siteDocument = getSiteDocument(getHtml("XXX"))
        val result = siteScanner.scanSite(siteDocument)
        result.scanStatus.shouldBe(InvalidPrice)
    }

    private fun getHtml(price: String): String = "<html><body>" +
        "<price product_id=\"123456\">$price</price>" +
        "</body></html>"

    private fun getSiteDocument(
        siteHtml: String,
        cssSelector: String = "body > price"
    ): SiteDocument {
        val product = Product("Nike Air Max")
        val site = Site("ASOS", cssSelector)
        val siteItem = SiteItem("asos.com?pid=1", ONE, product, site)
        return SiteDocument(siteItem, siteHtml)
    }
}