package org.icognition.salechecker

import mu.KLogging
import org.icognition.salechecker.entity.Product
import org.icognition.salechecker.entity.Site
import org.icognition.salechecker.entity.SiteItem
import org.icognition.salechecker.repository.ProductRepository
import org.icognition.salechecker.repository.SiteItemRepository
import org.icognition.salechecker.repository.SiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.BasicQuery
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.stereotype.Component

@Component
class DataInitialiser {

  companion object : KLogging()

  @Autowired
  lateinit var reactiveMongoOperations: ReactiveMongoOperations
  @Autowired
  lateinit var siteItemRepository: SiteItemRepository
  @Autowired
  lateinit var siteRepository: SiteRepository
  @Autowired
  lateinit var productRepository: ProductRepository

  fun initialise() {

    //saveProducts()
    //saveSites()
    saveSiteItems()
    logger.info { "Data initialisation complete" }
  }

  private fun saveSiteItems() {
    val siteItems = mapOf(
      Pair(
        "https://www.harrods.com/en-gb/paige/federal-shorts-p000000000006273000",
        Triple(
          "5daf79e83b715f61f9a96f43", // site
          "5d9df728400e342a38bf45b4", // product
          165.00
        )
      )
    )
    saveSiteItems(siteItems)
  }

  private fun saveProducts() {
    val products = listOf(
      "Paige Denim Shorts"
    )
    products.forEach {
      productRepository.save(Product(it)).block()
    }
  }

  private fun saveSites() {
    val sites = listOf(
//        Pair("Vitkac", "#prod_price"),
//        Pair("End Clothing", "#pdp__details__final-price"),
//        Pair("Rag & Bone", "#pdp__details__final-price"),
//        Pair("East Dane", "#pdp-pricing > span.pdp-price"),
//        Pair("Farfetch", "#slice-pdp > div > div.c6e09d > div.d31d3a > div._5ebd85 > " +
//            "span:nth-child(1) > strong"),
//        Pair("Goodhood", "#Product-Detail-Box > div > p > span > span:nth-child(1)"),
//        Pair("Woodhouse", "#product_addtocart_form > div.pdp_details.same-height > div" +
//            ".product-shop > div.price-info-size-wrapper > div.price-info > div.price-box > p > span.was-price"),
//        Pair("Mr Porter", "#product-page > div > section > section.product-details" +
//            ".js-product-details > span.product-details-price.undefined > span > span.product-details__price--value.price-sale"),
//        Pair("Matches Fashion", "#pdpMainWrapper > div.pdp__description-wrapper > div"
//            + ".pdp__header.hidden-mobile > p"),
//      Pair(
//        "Harvey Nichols",
//        "#page > div.content > div > div.pdp__coreinfo.u-clear-fix > div.p-details > div > div.p-details__price > div > p"
//      )
      Pair(
        "Harrods",
        "#main > section.pdp > div:nth-child(1) > div > section.pdp_main > section.pdp_buying-controls > section > form > div.buying-controls_details > div.buying-controls_price.js-buying-controls_price > div > span.price_amount"
      )
    )
    sites.forEach {
      siteRepository.save(Site(it.first, it.second)).block()
    }
  }

  private fun saveSiteItems(siteItems: Map<String, Triple<String, String, Double>>) {
    val query = BasicQuery.query(TextCriteria.forDefaultLanguage())
    reactiveMongoOperations.remove(query, SiteItem::class.java)
    //this.siteItemRepository
    //  .deleteAll()
    //  .subscribe()

    //val map: List<SiteItem> = siteItems
    //  .map {
    //    val site = siteRepository.findById(it.value.first).block()!!
    //    val product = productRepository.findById(it.value.second).block()!!
    //    SiteItem(it.key, BigDecimal.valueOf(it.value.third), product, site)
    //  }
    //val saved = siteItemRepository.saveAll(map)
    //logger.info { "Saved $saved.count().block() site items" }
  }
}