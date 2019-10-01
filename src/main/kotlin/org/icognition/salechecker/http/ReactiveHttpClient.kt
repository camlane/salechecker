package org.icognition.salechecker.http

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import kotlinx.coroutines.Dispatchers.IO as io
import org.springframework.http.MediaType.APPLICATION_JSON as json

@ExperimentalCoroutinesApi
@FlowPreview
@Component
class ReactiveHttpClient {

  suspend fun get(url: String): String {

    val webClient: WebClient = WebClient.builder().build()

    return withContext(io) {
      webClient.get()
          .uri(url)
          .accept(json)
          .retrieve()
          .awaitBody<String>()
    }

  }
}
