package org.icognition.salechecker.repository

import org.icognition.salechecker.entity.Site
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.remove

class UserRepository(
    private val mongo: ReactiveMongoOperations
) {

    fun findAll() = mongo.findAll<Site>()

    fun findOne(id: String) = mongo.findById<Site>(id)

    fun deleteAll() = mongo.remove<Site>()

    fun save(site: Site) = mongo.save(site)
}