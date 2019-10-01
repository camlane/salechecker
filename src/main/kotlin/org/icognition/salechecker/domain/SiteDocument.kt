package org.icognition.salechecker.domain

import org.icognition.salechecker.entity.SiteItem

data class SiteDocument(
    val siteItem: SiteItem,
    val siteHtml: String)
