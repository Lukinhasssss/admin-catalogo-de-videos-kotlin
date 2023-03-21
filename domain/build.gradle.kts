plugins {
    id("java-conventions")
    kotlin("jvm") version Version.kotlin
    id("jacoco")
    id("org.sonarqube") version Version.sonarqube
    id("org.jlleitschuh.gradle.ktlint") version Version.ktlint
    id("io.gitlab.arturbosch.detekt") version Version.detekt
}

group = "com.lukinhasssss.admin.catalogo.domain"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    testImplementation("io.github.serpro69:kotlin-faker:1.13.0")
}

configurations {
    create("testClasses") {
        extendsFrom(testImplementation.get())
    }
}

tasks.getByName("assemble").dependsOn("testJar")

tasks.register<Jar>("testJar") {
    archiveClassifier.set("test")
    from(project.the<SourceSetContainer>()["test"].output)
}

artifacts {
    testImplementation(tasks.getByName("testJar"))
}
