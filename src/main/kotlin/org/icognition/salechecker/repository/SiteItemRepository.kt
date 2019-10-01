package org.icognition.salechecker.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.icognition.salechecker.entity.SiteItem
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
interface SiteItemRepository : ReactiveCrudRepository<SiteItem, String>

fun SiteItemRepository.findAllAsFlow(): Flow<SiteItem> {
  return this.findAll()
      .toStream()
      .collect(Collectors.toList())
      .toFlow()
}

fun List<SiteItem>?.toFlow(): Flow<SiteItem> {
  val list = this
  return flow {
    list!!.forEach { emit(it) }
  }
}
