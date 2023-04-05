import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java-conventions")
    id("application")
    id("jacoco")
    id("jacoco-report-aggregation")
    id("org.sonarqube") version Version.SONARQUBE
    id("org.jlleitschuh.gradle.ktlint") version Version.KTLINT
    id("io.gitlab.arturbosch.detekt") version Version.DETEKT
    id("org.springframework.boot") version Version.SPRING_BOOT
    id("io.spring.dependency-management") version Version.SPRING_DEPENDENCY_MANAGEMENT
    id("org.flywaydb.flyway") version Version.FLYWAY
    kotlin("jvm") version Version.KOTLIN
    kotlin("plugin.spring") version Version.KOTLIN
    kotlin("plugin.jpa") version Version.KOTLIN
}

group = "com.lukinhasssss.admin.catalogo.infrastructure"
// version = "1.0-SNAPSHOT"
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

    implementation("org.springdoc:springdoc-openapi-webmvc-core:1.6.15")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.15")

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow") {
        exclude(group = "io.undertow", module = "undertow-websockets-jsr")
    }

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    implementation("org.postgresql:postgresql")
    implementation("org.hibernate:hibernate-validator:8.0.0.Final")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus:1.10.5")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("net.logstash.logback:logstash-logback-encoder:7.3")

    implementation("com.google.cloud:google-cloud-storage:2.20.1")
    implementation("com.google.guava:guava:31.1-jre")

    testImplementation(project(path = ":domain", configuration = "testClasses"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("org.flywaydb:flyway-core:${Version.FLYWAY}")
    testImplementation("com.h2database:h2")

    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("io.rest-assured:kotlin-extensions:5.3.0")

    testImplementation("org.testcontainers:testcontainers:${Version.TEST_CONTAINERS}")
    testImplementation("org.testcontainers:postgresql:${Version.TEST_CONTAINERS}")
    testImplementation("org.testcontainers:junit-jupiter:${Version.TEST_CONTAINERS}")
    testImplementation("com.github.dasniko:testcontainers-keycloak:2.5.0")
    testImplementation("org.jboss.resteasy:resteasy-core:4.7.9.Final")
    testImplementation("org.jboss.resteasy:resteasy-multipart-provider:4.7.9.Final")
}

flyway {
    url = System.getenv("FLYWAY_DB") ?: "jdbc:postgresql://localhost:5432/adm_videos"
    user = System.getenv("FLYWAY_USER") ?: "lukinhasssss"
    password = System.getenv("FLYWAY_PASSWORD") ?: "348t7y30549g4qptbq4rtbq4b5rq3rvq34rfq3784yq23847yqor78hvgoreiuvn"

    cleanDisabled = false
}

tasks.testCodeCoverageReport {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("${projectDir.parentFile.path}/build/reports/jacoco/test/jacocoTestReport.xml"))

        html.required.set(true)
        html.outputLocation.set(file("${projectDir.parentFile.path}/build/reports/jacoco/test/"))
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.testCodeCoverageReport)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
