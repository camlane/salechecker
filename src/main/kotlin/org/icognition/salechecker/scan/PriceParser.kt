package org.icognition.salechecker.scan

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*
import java.util.Locale.UK

@Component
class PriceParser {

  fun parse(price: String): BigDecimal = price.stripCurrencyCharacters().toBigDecimal()

}

private fun String.stripCurrencyCharacters(): String {
  val currency = Currency.getInstance(UK)
  val currencyCode = currency.currencyCode
  val currencySymbol = currency.symbol
  return this
      .removeMatching(currencyCode)
      .removeMatching(currencySymbol)
      .trim()
}

fun String.removeMatching(matching: String): String {
  return this.replace(matching, "", ignoreCase = true)
}