plugins {
    id("java-conventions")
    kotlin("jvm") version Version.kotlin
    id("jacoco")
    id("org.sonarqube") version Version.sonarqube
    id("org.jlleitschuh.gradle.ktlint") version Version.ktlint
    id("io.gitlab.arturbosch.detekt") version Version.detekt
}

group = "com.lukinhasssss.admin.catalogo.application"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))

    testImplementation(project(path = ":domain", configuration = "testClasses"))

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.4")
}
