package org.icognition.salechecker.repository

import org.icognition.salechecker.entity.Product
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component

@Component
interface ProductRepository : ReactiveCrudRepository<Product, String>
