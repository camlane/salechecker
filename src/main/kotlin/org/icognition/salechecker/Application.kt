package org.icognition.salechecker

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.icognition.salechecker.service.SiteCheckService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@ExperimentalCoroutinesApi
@FlowPreview
@EnableMongoAuditing
@EnableScheduling
@SpringBootApplication
class Application : CommandLineRunner {

  @Autowired
  lateinit var siteCheckService: SiteCheckService

  override fun run(args: Array<String>) {
    GlobalScope.launch {
      siteCheckService.checkSites()
    }
  }

  companion object {

    @JvmStatic
    fun main(args: Array<String>) {
      SpringApplication.run(Application::class.java, *args)
    }
  }

}
