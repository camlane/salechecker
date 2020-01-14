package org.icognition.salechecker.http

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withContext
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import kotlinx.coroutines.Dispatchers.IO as io
import org.springframework.http.MediaType.APPLICATION_JSON as json

@ExperimentalCoroutinesApi
@FlowPreview
@Component
class ReactiveHttpClient {

    companion object : KLogging()

    suspend fun get(url: String): ResponseEntity<String> {

        logger.info { "Retrieving url $url" }
        val webClient: WebClient = WebClient.builder().build()

        return withContext(io) {
            webClient.get()
                .uri(url)
                .accept(json)
                .retrieve()
                .toEntity(String::class.java)
                .awaitSingle()
        }
    }
}
