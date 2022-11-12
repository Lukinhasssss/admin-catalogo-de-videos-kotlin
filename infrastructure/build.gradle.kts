import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    id("application")
    id("jacoco")
    id("org.sonarqube") version "3.5.0.2730"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0-RC2"
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.flywaydb.flyway") version "9.7.0"
}

group = "com.lukinhasssss.admin.catalogo.infrastructure"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<BootJar> {
    archiveBaseName.set("application.jar")
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

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow") {
        exclude(group = "io.undertow", module = "undertow-websockets-jsr")
    }
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
    testImplementation("org.flywaydb:flyway-core:9.7.0")
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
        freeCompilerArgs = listOf("-Xuse-k2", "-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}
