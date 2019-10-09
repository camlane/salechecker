package org.icognition.salechecker.repository

import io.kotlintest.matchers.equality.shouldBeEqualToIgnoringFields
import org.icognition.salechecker.entity.Site
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
@SpringBootTest
class SiteRepositoryTest {

  @Autowired
  lateinit var siteRepository: SiteRepository

  @BeforeEach
  fun setUp() {
    siteRepository.deleteAll().block()
  }

  @Test
  fun `saved sites are found in the repository`() {
    val site = Site("nike.com", "body > p")
    val site2 = Site("asos.com", "body > p")

    siteRepository.saveAll(Flux.just(site, site2)).subscribe()

    StepVerifier.create(siteRepository.findAll())
        .assertNext { it.shouldBeEqualToIgnoringFields(site, site::id) }
        .assertNext { it.shouldBeEqualToIgnoringFields(site2, site2::id) }
        .verifyComplete()
  }

}