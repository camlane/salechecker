package org.icognition.salechecker.entity

import org.icognition.salechecker.entity.SiteItem.SiteItemStatus.Active
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document("siteItems")
data class SiteItem(
    @Indexed(unique = true) val url: String,
    val originalPrice: BigDecimal,
    @DBRef val product: Product,
    @DBRef val site: Site,
    val status: SiteItemStatus = Active,
    @Id val id: String? = null) {

  enum class SiteItemStatus {
    Active,
    Inactive
  }
}