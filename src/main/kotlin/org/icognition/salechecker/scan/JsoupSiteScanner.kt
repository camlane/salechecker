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

@Component
class JsoupSiteScanner(val priceParser: PriceParser) {

  companion object : KLogging()

  fun scanSite(siteDocument: SiteDocument): ScanResult {

    val siteItem = siteDocument.siteItem
    logger.info { "Scanning url $siteItem.url" }

    val document = Jsoup.parseBodyFragment(siteDocument.siteHtml)
    val elements = document.select(siteItem.site.cssSelector)
    if (elements.firstElementTextIsEmpty()) {
      return scanResult(siteDocument, ElementNotFound)
    }

    val result = kotlin.runCatching {
      priceParser.parse(elements.firstElementText())
    }

    return if (result.isFailure) {
      scanResult(siteDocument, InvalidPrice)
    } else {
      scanResult(siteDocument, ElementFound, result.getOrNull())
    }
  }

  private fun scanResult(siteDocument: SiteDocument,
                         scanStatus: ScanResult.ScanStatus,
                         scanPrice: BigDecimal? = null)
      : ScanResult = ScanResult(scanPrice,
      OK,
      scanStatus,
      siteDocument.siteItem)

}

private fun Elements.firstElementTextIsEmpty(): Boolean =
    this.isEmpty() || firstElementText().isEmpty()

private fun Elements.firstElementText() = this.first().ownText()