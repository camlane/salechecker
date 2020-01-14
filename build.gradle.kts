import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.61"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.61"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "org.icognition"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

val kotlinVersion = "1.3.61"
val kotlinCoroutinesVersion = "1.3.2"
val springBootVersion = "2.2.1.RELEASE"
val springDataMongoVersion = "2.2.1.RELEASE"
val reactorVersion = "3.3.0.RELEASE"
val junit5Version = "5.5.2"
val junit5PlatformVersion = "1.5.2"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", kotlinCoroutinesVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor", kotlinCoroutinesVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-html-jvm", "0.6.10")
    implementation("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
    implementation("org.springframework.boot", "spring-boot", springBootVersion)
    implementation("org.springframework.boot", "spring-boot-starter-mail", springBootVersion)
    implementation("org.springframework.boot", "spring-boot-starter-webflux", springBootVersion)
    implementation("org.springframework.data", "spring-data-mongodb", springDataMongoVersion)
    implementation("org.springframework.boot", "spring-boot-starter-data-mongodb-reactive", springBootVersion)
    implementation("org.springframework.fu:spring-fu-kofu:0.2.2")
    implementation("io.projectreactor", "reactor-core", reactorVersion)
    implementation("io.arrow-kt", "arrow-core", "0.10.0")
    implementation("io.github.microutils", "kotlin-logging", "1.7.8")
    implementation("org.jsoup", "jsoup", "1.12.1")
    implementation("ch.qos.logback", "logback-classic", "1.2.3")

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
    implementation("de.flapdoodle.embed", "de.flapdoodle.embed.mongo", "2.2.0")
    testImplementation("io.kotlintest", "kotlintest-assertions", "3.4.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junit5Version)
    testRuntimeOnly("org.junit.platform", "junit-platform-commons", junit5PlatformVersion)
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher", junit5PlatformVersion)
    testImplementation("io.kotlintest", "kotlintest-core", "3.4.2")
    testImplementation("io.kotlintest", "kotlintest-assertions", "3.4.2")
    testRuntimeOnly("io.kotlintest", "kotlintest-runner-junit5", "3.4.2")
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

tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes(mapOf("Main-Class" to "org.icognition.salechecker.ApplicationKt"))
        }
    }
}


configurations.all {
    exclude(module = "classgraph")
    exclude(module = "kindedj")
    exclude(module = "apiguardian-api")
    exclude(module = "jboss-logging")
    exclude(module = "hibernate-validator")
    exclude(module = "diffutils")
    exclude(module = "univocity-parsers")
    exclude(module = "jul-to-sllf4j")
    exclude(module = "xmlunit-core")
    exclude(module = "nio-multipart-parser")
    exclude(module = "jul-to-slf4j")
    exclude(module = "jakarta.el")
    exclude(module = "commons-io")
    exclude(group = "org.apache.logging.log4j")
    exclude(group = "net.java.dev.jna")
    exclude(group = "jakarta.xml.bind")
    exclude(group = "jakarta.validation")
    exclude(group = "net.minidev")
}
