import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Version.kotlin
    id("jacoco")
    id("org.sonarqube") version Version.sonarqube
    id("org.jlleitschuh.gradle.ktlint") version Version.ktlint
    id("io.gitlab.arturbosch.detekt") version Version.detekt
}

group = "com.lukinhasssss.admin.catalogo.domain"
version =

    repositories {
        mavenCentral()
    }

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.github.serpro69:kotlin-faker:1.13.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        useK2 = false
        javaParameters = true
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
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
