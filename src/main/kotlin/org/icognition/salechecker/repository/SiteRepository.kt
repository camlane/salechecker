package org.icognition.salechecker.repository

import org.icognition.salechecker.entity.Site
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component

@Component
interface SiteRepository : ReactiveCrudRepository<Site, String>
