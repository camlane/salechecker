package org.icognition.salechecker.scan

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class PriceParserTest {

  private val priceParser = PriceParser()

  @Test
  fun `throws exception when not a valid number`() {

    val text = "XXX.XX"

    assertFailsWith<NumberFormatException> { priceParser.parse(text) }
  }

  @Test
  fun `text with whitespace parses successfully`() {

    val text = " 123.45 "

    val result = priceParser.parse(text)
    result.shouldBe(BigDecimal("123.45"))
  }

  @Test
  fun `text containing currency symbol parses successfully`() {
    val text = "£123.45"

    val result = priceParser.parse(text)
    result.shouldBe(BigDecimal("123.45"))
  }

  @Test
  fun `text containing currency code parses successfully`() {
    val text = "123.45 GBP"

    val result = priceParser.parse(text)
    result.shouldBe(BigDecimal("123.45"))
  }

}