package org.icognition.salechecker.repository

import io.kotlintest.matchers.equality.shouldBeEqualToIgnoringFields
import org.icognition.salechecker.entity.Product
import org.icognition.salechecker.entity.Site
import org.icognition.salechecker.entity.SiteItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.math.BigDecimal.ONE
import kotlin.test.assertFailsWith

@ExtendWith(SpringExtension::class)
@SpringBootTest
class SiteItemRepositoryTest {
 
  @Autowired
  lateinit var siteRepository: SiteRepository
  @Autowired
  lateinit var siteItemRepository: SiteItemRepository
  @Autowired
  lateinit var productRepository: ProductRepository

  @BeforeEach
  fun setUp() {
    siteRepository.deleteAll().block()
    productRepository.deleteAll().block()
    siteItemRepository.deleteAll().block()
  }

  @Test
  fun `saved site items are found in repository`() {

    val product = productRepository.save(Product("Nike Air Max")).block()!!
    val site = siteRepository.save(Site("Nike", "body > p")).block()!!

    val siteItem = SiteItem("nike.com?pid=10", ONE, product, site)
    val siteItem1 = SiteItem("nike.com?pid=12", ONE, product, site)
    siteItemRepository.saveAll(Flux.just(siteItem, siteItem1)).subscribe()

    StepVerifier.create(siteItemRepository.findAll())
        .assertNext { it.shouldBeEqualToIgnoringFields(siteItem, siteItem::id) }
        .assertNext { it.shouldBeEqualToIgnoringFields(siteItem1, siteItem1::id) }
        .verifyComplete()
  }

  @Test
  fun `duplicate key error thrown when site items with same url are saved`() {

    val product = productRepository.save(Product("Mens T-shirt")).block()!!
    val site = siteRepository.save(Site("Asos", "body > price")).block()!!

    siteItemRepository.save(SiteItem("asos.com?pid=1", ONE, product, site)).block()
    assertFailsWith(java.lang.Exception::class, "E11000 duplicate key error collection") {
      siteItemRepository.save(SiteItem("asos.com?pid=1", ONE, product, site)).block()
    }
  }


}