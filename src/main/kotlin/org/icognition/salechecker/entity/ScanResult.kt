package org.icognition.salechecker.entity

import org.icognition.salechecker.entity.ScanResult.ScanStatus.None
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDateTime

@Document("scanResults")
data class ScanResult(
    val scanPrice: BigDecimal?,
    val httpStatus: HttpStatus,
    val scanStatus: ScanStatus = None,
    @DBRef val siteItem: SiteItem,
    @CreatedDate val createdDate: LocalDateTime = LocalDateTime.now(),
    @Id val id: String? = null) {

  enum class ScanStatus {
    None,
    ElementFound,
    ElementNotFound,
    InvalidPrice
  }
}
