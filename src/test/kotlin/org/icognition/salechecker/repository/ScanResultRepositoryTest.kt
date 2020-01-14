package org.icognition.salechecker.repository

import io.kotlintest.shouldBe
import org.icognition.salechecker.entity.Product
import org.icognition.salechecker.entity.ScanResult
import org.icognition.salechecker.entity.ScanResult.ScanStatus.ElementFound
import org.icognition.salechecker.entity.ScanResult.ScanStatus.ElementNotFound
import org.icognition.salechecker.entity.Site
import org.icognition.salechecker.entity.SiteItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ScanResultRepositoryTest {

  @Autowired
  lateinit var productRepository: ProductRepository
  @Autowired
  lateinit var siteItemRepository: SiteItemRepository
  @Autowired
  lateinit var siteRepository: SiteRepository
  @Autowired
  lateinit var scanResultRepository: ScanResultRepository

  @Autowired
  lateinit var reactiveMongoTemplate: ReactiveMongoTemplate

  @BeforeEach
  fun setup() {
    this.reactiveMongoTemplate.remove(ScanResult::class.java).all()
      .subscribe()
  }

  @Test
  fun `scan result saved`() {
    val product = saveProduct("Nike Air Max")
    val site = saveSite("Nike")
    val siteItem = saveSiteItem(product, site, "nike.com?pid=1", ONE)

    val scanResult = ScanResult(ONE, OK, ElementFound, siteItem)

    StepVerifier.create(this.scanResultRepository.save(scanResult))
      .consumeNextWith { (scanPrice) -> scanPrice.shouldBe(ONE) }
      .expectComplete()
      .verify()
  }

  @Test
  fun `scan results found when multiple saved`() {
    val product = saveProduct("Havaianas")
    val site = saveSite("asos")
    val siteItem = saveSiteItem(product, site, "asos.com?pid=10", ONE)

    val scanResult1 = ScanResult(TEN, OK, ElementFound, siteItem)
    val scanResult2 = ScanResult(ONE, OK, ElementFound, siteItem)

    val allScanResults = Flux.just(scanResult1, scanResult2)
      .flatMap { result -> scanResultRepository.save(result) }
      .thenMany(
        scanResultRepository.findByHttpStatusAndScanStatus(
          OK,
          ElementFound
        )
      )

    StepVerifier.create(allScanResults)
      .expectNextMatches { (scanPrice) -> scanPrice == TEN }
      .expectNextMatches { (scanPrice) -> scanPrice == ONE }
      .verifyComplete()
  }

  @Test
  fun `scan results with matching scan and http status are found`() {

    val product = saveProduct("Yeezy 500")
    val site = saveSite("Adidas")
    val siteItem = saveSiteItem(product, site, "adidas.com?pid=1", ONE)

    val scanResult1 = ScanResult(TEN, OK, ElementNotFound, siteItem)
    val scanResult2 = ScanResult(ONE, OK, ElementFound, siteItem)

    val scanResults = Flux.just(scanResult1, scanResult2)
      .flatMap { result -> scanResultRepository.save(result) }
      .thenMany(
        scanResultRepository.findByHttpStatusAndScanStatus(
          OK,
          ElementFound
        )
      )

    StepVerifier.create(scanResults)
      .expectNextMatches { (scanPrice) -> scanPrice == ONE }
      .verifyComplete()
  }

  @Test
  fun `only scan results with ok httpStatus are found`() {

    val product = saveProduct("Superdry T-shirt")
    val site = saveSite("Superdry")
    val siteItem = saveSiteItem(product, site, "superdry.com?pid=1", ONE)

    val scanResult1 = ScanResult(TEN, OK, ElementFound, siteItem)
    val scanResult2 = ScanResult(ONE, NOT_FOUND, ElementFound, siteItem)

    val allScanResults = Flux.just(scanResult1, scanResult2)
      .flatMap { result -> scanResultRepository.save(result) }
      .thenMany(
        scanResultRepository.findByHttpStatusAndScanStatus(
          OK,
          ElementFound
        )
      )

    StepVerifier.create(allScanResults)
      .expectNextMatches { (scanPrice) -> scanPrice == TEN }
      .verifyComplete()
  }

  private fun saveSiteItem(
    product: Product,
    site: Site,
    url: String,
    price: BigDecimal
  ): SiteItem {
    val toSave = SiteItem(url, price, product, site)
    return siteItemRepository.save(toSave).block()!!
  }

  private fun saveProduct(name: String) =
    productRepository.save(Product(name)).block()!!

  private fun saveSite(name: String, cssSelector: String = "body > p") =
    siteRepository.save(Site(name, cssSelector)).block()!!
}