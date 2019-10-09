import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  idea
  kotlin("jvm") version "1.3.41"
  id("org.jetbrains.kotlin.plugin.allopen") version "1.3.50"
  id("org.jetbrains.kotlin.plugin.spring") version "1.3.50"
}

group = "org.icognition"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  jcenter()
  maven { url = uri("https://repo.spring.io/milestone") }
  maven { url = uri("https://repo.spring.io/snapshot") }
}

val kotlinVersion = "1.3.50"
val kotlinCoroutinesVersion = "1.3.2"
val springBootVersion = "2.2.0.M6"
val springDataMongoVersion = "2.1.10.RELEASE"
val reactorVersion = "3.3.0.RELEASE"
val junit5Version = "5.5.2"
val junit5PlatformVersion = "1.5.2"

dependencies {
  implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", kotlinCoroutinesVersion)
  implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor", kotlinCoroutinesVersion)
  implementation("org.jetbrains.kotlinx", "kotlinx-html-jvm", "0.6.10")

  implementation("org.jsoup", "jsoup", "1.12.1")
  implementation("org.springframework.boot", "spring-boot", springBootVersion)
  implementation("org.springframework.boot", "spring-boot-starter-mail", springBootVersion)
  implementation("org.springframework.boot", "spring-boot-starter-webflux", springBootVersion)
  implementation("org.springframework.data", "spring-data-mongodb", springDataMongoVersion)
  implementation("org.springframework.boot", "spring-boot-starter-data-mongodb-reactive", springBootVersion)
  implementation("io.projectreactor", "reactor-core", reactorVersion)
  implementation("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
  implementation("io.github.microutils", "kotlin-logging", "1.7.6")
  implementation(kotlin("stdlib-jdk8"))
  runtimeOnly("ch.qos.logback", "logback-classic", "1.2.3")

  testImplementation("org.junit.jupiter", "junit-jupiter-api", junit5Version)
  testImplementation("io.projectreactor", "reactor-test", reactorVersion)
  testImplementation("org.hamcrest", "hamcrest-core", "2.1")
  testImplementation("org.springframework.boot", "spring-boot-starter-test", springBootVersion) {
    exclude(module = "junit")
    exclude(module = "mockito-core")
  }

  testImplementation("io.mockk:mockk:1.9.3")
  testImplementation("org.jetbrains.kotlin", "kotlin-test", kotlinVersion)
  testImplementation("org.jetbrains.kotlin", "kotlin-test-common", kotlinVersion)
  testImplementation("org.jetbrains.kotlin", "kotlin-test-annotations-common", kotlinVersion)
  testImplementation("de.flapdoodle.embed", "de.flapdoodle.embed.mongo", "2.2.0")
  testImplementation("io.kotlintest", "kotlintest-assertions", "3.4.2")
  testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
  testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junit5Version)
  testRuntimeOnly("org.junit.platform", "junit-platform-commons", junit5PlatformVersion)
  testRuntimeOnly("org.junit.platform", "junit-platform-launcher", junit5PlatformVersion)
}

tasks.withType<Test> {
  useJUnitPlatform()
}

allOpen {
  annotation("org.springframework.data.mongodb.core.mapping.Document")
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
    }
  }
}
