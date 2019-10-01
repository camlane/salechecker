package org.icognition.salechecker.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("products")
data class Product (
		@Id var description: String,
		var manufacturer: String? = null,
		var id: String? = null
)
