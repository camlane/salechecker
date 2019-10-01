package org.icognition.salechecker.repository

import org.icognition.salechecker.entity.ScanResult
import org.icognition.salechecker.entity.ScanResult.ScanStatus
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
interface ScanResultRepository : ReactiveCrudRepository<ScanResult, String> {

	fun findByHttpStatusAndScanStatus(httpStatus: HttpStatus,
																		scanStatus: ScanStatus): Flux<ScanResult>

}
