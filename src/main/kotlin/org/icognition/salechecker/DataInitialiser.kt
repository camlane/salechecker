package org.icognition.salechecker

import org.icognition.salechecker.repository.ProductRepository
import org.icognition.salechecker.repository.SiteItemRepository
import org.icognition.salechecker.repository.SiteRepository
import org.springframework.stereotype.Component

@Component
class DataInitialiser {

  private val siteItemRepository: SiteItemRepository? = null
  private val siteRepository: SiteRepository? = null
  private val productRepository: ProductRepository? = null

  internal fun initialise() {

    //    saveProducts();
    //    saveSites();
    saveSiteItems()
  }

  private fun saveSiteItems() {
    val siteItems = mapOf(Pair(
        "https://www.eastdane.com/fit-beach-shirt-rag-bone/vp/v=1/1540634866.htm",
        Triple(
            "5cb31e924c10441a82146b61", // site
            "5cb31e924c10441a82146b5c", // product
            172.04)),
        Pair("https://www.mrporter.com/en-gb/mens/rag_and_bone/fit-3-beach-checked-herringbone-cotton-blend-shirt/1093201",
            Triple(
                "5cb31e924c10441a82146b5a", // site
                "5cb31e924c10441a82146b5c", // product
                160.0)),
        Pair("https://www.harveynichols.com/brand/rag-and-bone/303334-beach-checked-cotton-shirt/p3354521",
            Triple(
                "5cb31e924c10441a82146b6c", // site
                "5cb31e924c10441a82146b5c", // product
                150.0)),
        Pair("https://www.farfetch.com/uk/shopping/men/rag-bone-logo-crew-neck-t-shirt-item-13908313.aspx",
            Triple(
                "5cb31e924c10441a82146b6b", // site
                "5cb31e924c10441a82146b66", // product
                100.0)),
        Pair("https://www.rag-bone.com/mens/bottoms/fit-2-flyweight-chino-M1723YGWCNAV.html",
            Triple(
                "5cb31e924c10441a82146b65", // site
                "5cb31e924c10441a82146b5f", // product
                170.0)),
        Pair("https://www.endclothing.com/gb/rag-bone-standard-issue-skinny-jean-m1224k960ris.html",
            Triple(
                "5cb31e924c10441a82146b62", // site
                "5cb31e924c10441a82146b60", // product
                169.0)),
        Pair("https://www.rag-bone.com/mens/new-arrivals/stockton-cardigan-M292603KD.html",
            Triple(
                "5cb31e924c10441a82146b65", // site
                "5cb31e924c10441a82146b64", // product
                265.0)),
        Pair("https://goodhoodstore.com/store/norse-projects-hans-summer-check-shirt-russet-42013",
            Triple(
                "5cb31e924c10441a82146b5e", // site
                "5cb31e924c10441a82146b69", // product
                120.0)),
        Pair("https://www.harveynichols.com/brand/rag-and-bone/319449-black-cotton-t-shirt/p3435006",
            Triple(
                "5cb31e924c10441a82146b6c", // site
                "5cb31e924c10441a82146b66", // product
                95.0))
    )
    saveSiteItems(siteItems)
  }

  private fun saveProducts() {
    val products = listOf(
        "Dagger T-Shirt Navy",
        "Beach checked shirt Yellow",
        "Flyweight chino",
        "Selvedge skinny jeans",
        "Raw Indigo jeans",
        "Stockton cardigan",
        "Logo crew neck T-Shirt",
        "Fit 3 Beach Shirt check blue multi colour",
        "Hans Summer Check Shirt"
    )
  }

  private fun saveSites() {
    val sites = mapOf(
        Pair("Vitkac", "#prod_price"),
        Pair("End Clothing", "#pdp__details__final-price"),
        Pair("Rag & Bone", "#pdp__details__final-price"),
        Pair("East Dane", "#pdp-pricing > span.pdp-price"),
        Pair("Farfetch", "#slice-pdp > div > div.c6e09d > div.d31d3a > div._5ebd85 > " +
            "span:nth-child(1) > strong"),
        Pair("Goodhood", "#Product-Detail-Box > div > p > span > span:nth-child(1)"),
        Pair("Woodhouse", "#product_addtocart_form > div.pdp_details.same-height > div" +
            ".product-shop > div.price-info-size-wrapper > div.price-info > div.price-box > p > span.was-price"),
        Pair("Mr Porter", "#product-page > div > section > section.product-details" +
            ".js-product-details > span.product-details-price.undefined > span > span.product-details__price--value.price-sale"),
        Pair("Matches Fashion", "#pdpMainWrapper > div.pdp__description-wrapper > div"
            + ".pdp__header.hidden-mobile > p"),
        Pair("Harvey Nichols", "#page > div.content > div > div" +
            ".pdp__coreinfo.u-clear-fix > div.p-details > div > div.p-details__price > div > p")
    )
  }

  private fun saveSiteItems(siteItems: Map<String, Triple<String, String, Double>>) {
    this.siteItemRepository!!
        .deleteAll()
        .subscribe()

//    siteItemRepository.saveAll(siteItems
//        .map { SiteItem(it.key, BigDecimal.valueOf(it.value.third) })
  }
//
//	private fun saveProducts(products: List<String>) {
//		this.productRepository!!
//				.deleteAll()
//				.thenMany(Flux
//						.fromIterable(products)
//						.flatMap<Product>(Function<String, Publisher<out Product>> { this.saveProduct(it) })
//				)
//				.log()
//				.subscribe(null, null, { log.info("done product initialization...") })
//	}
//
//	private fun saveSites(sites: Map<String, String>) {
//		this.siteRepository!!
//				.deleteAll()
//				.thenMany(Flux
//						.fromIterable<Entry<String, String>>(sites.entries)
//						.flatMap<Site>(Function<Entry<String, String>, Publisher<out Site>> { this.saveProduct(it) })
//				)
//				.log()
//				.subscribe(null, null, { log.info("done site initialization...") })
//	}
//
//	private fun saveProduct(entry: Entry<String, String>): Mono<Site> {
//		return this.siteRepository!!.save(Site(entry.key, "test",
//				entry.value))
//	}
//
//	private fun saveProduct(description: String): Mono<Product> {
//		return this.productRepository!!.save(Product(description))
//	}

//	private fun saveSiteItem(
//			entry: MutableMap.MutableEntry) {
//		val url = entry.key
//		val value = entry.value
//		val site = siteRepository!!.findById(value.getLeft()).block()
//		val product = productRepository!!.findById(value.getMiddle()).block()
//		val entity = SiteItem.newSite(url, BigDecimal(value.getRight()),
//				product!!, site!!)
//		return this.siteItemRepository!!.save(entity)
//	}

}