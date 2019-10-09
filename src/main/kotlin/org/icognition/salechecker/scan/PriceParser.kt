package org.icognition.salechecker.scan

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*
import java.util.Locale.UK

@Component
class PriceParser {

  @Throws(InvalidPriceTextException::class)
  fun parse(price: String): BigDecimal {
   try {
     return price
         .stripCurrencyCharacters()
         .toBigDecimal()
   } catch (e: NumberFormatException){
     throw InvalidPriceTextException(e)
   }
  }
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