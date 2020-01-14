package org.icognition.salechecker.scan

import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec
import io.kotlintest.specs.StringSpec
import java.math.BigDecimal

class MyTests : FeatureSpec({

    feature("Price Parsing")
    {
        scenario("throws exception when not a valid number") {
            // test here
        }
        scenario("text with whitespace parses successfully").config(enabled = false) {
            val text = " 123.45 "

            val result = PriceParser().parse(text)
            result.shouldBe(BigDecimal("123.45"))
        }
    }

})

class Person(name: String, age: Int)

class Tests : StringSpec({
    
    "text with whitespace parses successfully" {
        val text = " 123.45 "
        val result = PriceParser().parse(text)
        result.shouldBe(BigDecimal("123.45"))
    }

    "!text containing currency code parses successfully" {
        val text = " 123.45 "
        val result = PriceParser().parse(text)
        result.shouldBe(BigDecimal("123.45"))
    }
})
//    @Test
//    fun `text containing currency symbol parses successfully`() {
//        val text = "£123.45"
//
//        val result = priceParser.parse(text)
//        result.shouldBe(BigDecimal("123.45"))
//    }
//
//    @Test
//    fun `text containing currency code parses successfully`() {
//        val text = "123.45 GBP"
//
//        val result = priceParser.parse(text)
//        result.shouldBe(BigDecimal("123.45"))
//    }
//}