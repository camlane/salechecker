package org.icognition.salechecker.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("sites")
data class Site (
		val description: String,
		val cssSelector: String,
		@Id val id: String? = null) {

	enum class SiteStatus {
		ACTIVE,
		INACTIVE
	}

}
