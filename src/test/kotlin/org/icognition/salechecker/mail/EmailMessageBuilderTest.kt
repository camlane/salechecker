package org.icognition.salechecker.mail

import io.kotlintest.shouldBe
import org.icognition.salechecker.entity.Product
import org.icognition.salechecker.entity.ScanResult
import org.icognition.salechecker.entity.ScanResult.ScanStatus.ElementFound
import org.icognition.salechecker.entity.Site
import org.icognition.salechecker.entity.SiteItem
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.OK
import java.math.BigDecimal

class EmailMessageBuilderTest {

    private val emailMessageBuilder: EmailMessageBuilder = EmailMessageBuilder()

    @Test
    fun `message formats correctly`() {

        val site = Site("Asos", "body > p")
        val product = Product("Air Max 90")
        val siteItem = SiteItem("http://asos.com?pid=123", BigDecimal("129.95"), product, site)
        val scanResult = ScanResult(BigDecimal("80.95"), OK, ElementFound, siteItem)

        val result = emailMessageBuilder.generateMailMessage(scanResult)

        val expected = """
            <!DOCTYPE html>
            <table>
            <tr><td>Product</td><td>Air Max 90</td></tr>
            <tr><td>Site URL</td><td>http://asos.com?pid=123</td></tr>
            <tr><td>Original Price</td><td>&pound;129.95</td></tr>
            <tr><td>Latest Price</td><td>&pound;80.95</td></tr>
            </table>"""
        result.shouldBe(expected)
    }
}