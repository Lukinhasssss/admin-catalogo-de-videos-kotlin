import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    kotlin("plugin.jpa") version "1.8.0"
    id("application")
    id("jacoco")
    id("org.sonarqube") version "3.5.0.2730"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0-RC2"
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.flywaydb.flyway") version "9.10.2"
}

group = "com.lukinhasssss.admin.catalogo.infrastructure"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<BootJar> {
    archiveBaseName.set("application")
    destinationDirectory.set(file("${rootProject.buildDir}/libs"))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-afterburner")

    implementation("org.springdoc:springdoc-openapi-webmvc-core:1.6.14")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.14")

    implementation("io.vavr:vavr-kotlin:0.10.2")

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow") {
        exclude(group = "io.undertow", module = "undertow-websockets-jsr")
    }

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.hibernate:hibernate-validator:8.0.0.Final")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus:1.10.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
    testImplementation("org.flywaydb:flyway-core:9.10.2")

    testImplementation("io.github.serpro69:kotlin-faker:1.13.0")
    testImplementation("com.ninja-squad:springmockk:4.0.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("io.rest-assured:kotlin-extensions:5.3.0")

    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:postgresql:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
}

flyway {
    url = System.getenv("FLYWAY_DB") ?: "jdbc:postgresql://localhost:5432/adm_videos"
    user = System.getenv("FLYWAY_USER") ?: "lukinhasssss"
    password = System.getenv("FLYWAY_PASSWORD") ?: "348t7y30549g4qptbq4rtbq4b5rq3rvq34rfq3784yq23847yqor78hvgoreiuvn"

    cleanDisabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        useK2 = false
        javaParameters = true
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
