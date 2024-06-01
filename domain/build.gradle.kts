plugins {
    id("java-conventions")
    kotlin("jvm") version Version.KOTLIN
    id("jacoco")
    id("org.sonarqube") version Version.SONARQUBE
}

group = "com.lukinhasssss.admin.catalogo.domain"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    testImplementation("io.github.serpro69:kotlin-faker:1.13.0")
}

// Define version for plugins with vulnerabilities
configurations.all {
    resolutionStrategy {
        // failOnVersionConflict()

        eachDependency {
            if (requested.name == "kotlin-reflect") {
                useVersion(Version.KOTLIN)
            }
            if (requested.name == "kotlin-stdlib") {
                useVersion(Version.KOTLIN)
            }
            if (requested.name == "kotlin-stdlib-jdk7") {
                useVersion(Version.KOTLIN)
            }
            if (requested.name == "kotlin-stdlib-jdk8") {
                useVersion(Version.KOTLIN)
            }
            if (requested.name.startsWith("junit-jupiter")) {
                useVersion("5.9.2")
            }
            if (requested.name == "slf4j-api") {
                useVersion("2.0.0")
            }
        }
    }
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

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
