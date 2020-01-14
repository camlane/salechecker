package org.icognition.salechecker

import de.flapdoodle.embed.mongo.distribution.Version.Main.PRODUCTION
import kotlinx.coroutines.runBlocking
import org.icognition.salechecker.service.SiteCheckService
import org.springframework.boot.WebApplicationType.NONE
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.configuration
import org.springframework.fu.kofu.mongo.reactiveMongodb

//@EnableMongoAuditing
//@EnableScheduling

val dataConfig = configuration {
  beans {
    bean<DataInitialiser>()
    bean<SiteCheckService>()
  }
  listener<ApplicationReadyEvent> {
    runBlocking {
      ref<DataInitialiser>().initialise()
      ref<SiteCheckService>().checkSites()
    }
  }
  reactiveMongodb {
    embedded {
      version = PRODUCTION
    }
  }
}

val app = application(NONE) {
  enable(dataConfig)
}

fun main() {
  app.run()
}
