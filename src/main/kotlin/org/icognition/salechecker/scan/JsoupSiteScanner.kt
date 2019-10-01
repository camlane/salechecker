package org.icognition.salechecker.scan

import mu.KLogging
import org.icognition.salechecker.domain.SiteDocument
import org.icognition.salechecker.entity.ScanResult
import org.icognition.salechecker.entity.ScanResult.ScanStatus.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.springframework.http.HttpStatus.OK
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.text.ParseException

@Component
class JsoupSiteScanner(val priceParser: PriceParser) {

  companion object : KLogging()

  fun scanSite(siteDocument: SiteDocument): ScanResult {

    val siteItem = siteDocument.siteItem
    logger.info { "Scanning url $siteItem.url" }

    val document = Jsoup.parseBodyFragment(siteDocument.siteHtml)
    val elements = document.select(siteItem.site.cssSelector)
    if (elements.firstElementHasText()) {
      return scanResult(siteDocument, ELEMENT_NOT_FOUND)
    }

    val scanPrice =
        try {
          priceParser.parse(elements.firstElementText())
        } catch (e: ParseException) {
          return scanResult(siteDocument, INVALID_PRICE)
        }

    return scanResult(siteDocument, ELEMENT_FOUND, scanPrice)
  }

  private fun scanResult(siteDocument: SiteDocument,
                         scanStatus: ScanResult.ScanStatus,
                         scanPrice: BigDecimal? = null)
      : ScanResult = ScanResult(scanPrice,
      OK,
      scanStatus,
      siteDocument.siteItem)

}

private fun Elements.firstElementHasText(): Boolean =
    this.isEmpty() || firstElementText().isEmpty()

private fun Elements.firstElementText() = this.first().ownText()