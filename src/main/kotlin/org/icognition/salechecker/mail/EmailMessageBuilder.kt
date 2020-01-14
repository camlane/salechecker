package org.icognition.salechecker.mail

import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr
import org.icognition.salechecker.entity.ScanResult
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Component
class EmailMessageBuilder {

    fun generateMailMessage(scanResult: ScanResult): String {
        return createHTMLDocument()
            .table {
                tr {
                    td {
                        +"Product"
                    }
                    td {
                        +scanResult.siteItem.product.description
                    }
                }
                tr {
                    td {
                        +"Site URL"
                    }
                    td {
                        +scanResult.siteItem.url
                    }
                }
                tr {
                    td {
                        +"Original Price"
                    }
                    td {
                        +scanResult.siteItem.originalPrice.formatAsCurrency()
                    }
                }
                tr {
                    td {
                        +"Latest Price"
                    }
                    td {
                        +scanResult.scanPrice!!.formatAsCurrency()
                    }
                }
            }.serialize(false)
    }
}

private fun BigDecimal.formatAsCurrency(): String =
    getCurrencySymbol() + DecimalFormat.getInstance().format(this.toDouble())

private fun getCurrencySymbol(): String? =
    DecimalFormatSymbols.getInstance(Locale.UK).currencySymbol