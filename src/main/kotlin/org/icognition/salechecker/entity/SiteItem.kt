package org.icognition.salechecker.entity

import org.icognition.salechecker.entity.Site.SiteStatus
import org.icognition.salechecker.entity.Site.SiteStatus.ACTIVE
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
    val status: SiteStatus = ACTIVE,
    @Id val id: String? = null)