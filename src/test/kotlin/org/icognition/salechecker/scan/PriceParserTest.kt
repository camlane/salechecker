package org.icognition.salechecker.scan

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class PriceParserTest {

  private val priceParser: PriceParser = PriceParser()

  @BeforeEach
  fun setUp() {
  }

  @Test
  fun `when text contains spaces then parses successfully`() {

    val text = " 123.45 "

    val result = priceParser.parse(text)
    assertThat(result, `is`(BigDecimal("123.45")))
  }

  @Test
  fun `when text contains pound symbol then parses successfully`() {
    val text = "£123.45"

    val result = priceParser.parse(text)
    assertThat(result, `is`(BigDecimal("123.45")))
  }

  @Test
  fun `when text contains GBP then parses successfully`() {
    val text = "123.45 GBP"

    val result = priceParser.parse(text)
    assertThat(result, `is`(BigDecimal("123.45")))
  }

}