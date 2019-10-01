package org.icognition.salechecker.mail

import org.icognition.salechecker.entity.Product
import org.icognition.salechecker.entity.ScanResult
import org.icognition.salechecker.entity.Site
import org.icognition.salechecker.entity.SiteItem
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import kotlin.test.assertEquals

class EmailMessageBuilderTest {

  private val emailMessageBuilder: EmailMessageBuilder = EmailMessageBuilder()

  @Test
  fun `message formats correctly`() {

    val site = Site("Asos", "")
    val product = Product("Air Max 90", "Nike")
    val siteItem = SiteItem("http://asos.com?pid=123", BigDecimal("129.95"),
        product, site, Site.SiteStatus.ACTIVE)
    val scanResult = ScanResult(BigDecimal("80.95"), HttpStatus.OK,
        ScanResult.ScanStatus.ELEMENT_FOUND, siteItem)
    val result = emailMessageBuilder.generateMailMessage(scanResult)

    assertEquals("<!DOCTYPE html>\n" +
        "<table><tr><td>Product</td><td>Air Max 90</td></tr><tr><td>Site URL</td><td>http://asos.com?pid=123</td></tr><tr><td>Original Price</td><td>\$129.95</td></tr><tr><td>Latest Price</td><td>\$80.95</td></tr></table>", result)
  }

}