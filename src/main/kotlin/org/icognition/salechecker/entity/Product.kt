package org.icognition.salechecker.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("products")
data class Product(
    val description: String,
    val manufacturer: String? = null,
    @Id val id: String? = null
)
