import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java-conventions")
    id("application")
    id("jacoco")
    id("jacoco-report-aggregation")
    id("org.sonarqube") version Version.SONARQUBE
    id("org.springframework.boot") version Version.SPRING_BOOT
    id("io.spring.dependency-management") version Version.SPRING_DEPENDENCY_MANAGEMENT
    id("org.flywaydb.flyway") version Version.FLYWAY
    id("io.gatling.gradle") version Version.GATLING
    kotlin("jvm") version Version.KOTLIN
    kotlin("plugin.spring") version Version.KOTLIN
    kotlin("plugin.jpa") version Version.KOTLIN
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:${Version.FLYWAY}")
    }
}

group = "com.lukinhasssss.admin.catalogo.infrastructure"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_21

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

    implementation("org.springdoc:springdoc-openapi-webmvc-core:1.8.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.8.0")

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
    implementation("org.flywaydb:flyway-database-postgresql:${Version.FLYWAY}")
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.4")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.38.0")
    implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    implementation("com.google.cloud:google-cloud-storage:2.36.1")
    implementation("com.google.guava:guava:33.1.0-jre")

    testImplementation(project(path = ":domain", configuration = "testClasses"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("org.flywaydb:flyway-core:${Version.FLYWAY}")
    testImplementation("com.h2database:h2:2.2.220")

    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("io.rest-assured:kotlin-extensions:5.4.0")

    testImplementation("org.testcontainers:testcontainers:${Version.TEST_CONTAINERS}")
    testImplementation("org.testcontainers:postgresql:${Version.TEST_CONTAINERS}")
    testImplementation("org.testcontainers:junit-jupiter:${Version.TEST_CONTAINERS}")
    testImplementation("com.github.dasniko:testcontainers-keycloak:3.3.0")
    testImplementation("org.keycloak:keycloak-core:24.0.2")
}

// Define version for plugins with vulnerabilities
configurations.all {
    resolutionStrategy {
        failOnVersionConflict()

        eachDependency {
            if (requested.group == "org.jetbrains" && requested.name == "annotations") {
                useVersion("22.0.0")
            }
            if (requested.name == "httpclient") {
                useVersion("4.5.14")
            }
            if (requested.name == "junit-bom") {
                useVersion("5.10.1")
            }
            if (requested.name == "keycloak-common") {
                useVersion("24.0.2")
            }
            if (requested.name == "kotlinx-coroutines-bom") {
                useVersion("1.7.3")
            }
            if (requested.name == "opentest4j") {
                useVersion("1.3.0")
            }
            if (requested.name == "scala-compiler") {
                useVersion("2.13.11")
            }
            if (requested.name == "scala-reflect") {
                useVersion("2.13.11")
            }
            if (requested.name == "scala-library") {
                useVersion("2.13.11")
            }
            if (requested.name == "sjson-new-core_2.13") {
                useVersion("0.9.1")
            }
            if (requested.name == "jna") {
                useVersion("5.13.0")
            }
            if (requested.name == "checker-qual") {
                useVersion("3.42.0")
            }
            if (requested.name == "jboss-threads") {
                useVersion("3.5.0.Final")
            }
            if (requested.name == "wildfly-common") {
                useVersion("1.5.4.Final")
            }
        }
    }
}

flyway {
    url = System.getenv("FLYWAY_DB") ?: "jdbc:postgresql://localhost:5432/adm_videos"
    user = System.getenv("FLYWAY_USER") ?: "lukinhasssss"
    password = System.getenv("FLYWAY_PASSWORD") ?: "348t7y30549g4qptbq4rtbq4b5rq3rvq34rfq3784yq23847yqor78hvgoreiuvn"

    locations = arrayOf("classpath:db/migration/main")

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
