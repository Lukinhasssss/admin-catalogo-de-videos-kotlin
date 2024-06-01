plugins {
    id("java-conventions")
    kotlin("jvm") version Version.KOTLIN
    id("jacoco")
    id("org.sonarqube") version Version.SONARQUBE
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
            if (requested.name == "kotlin-stdlib-common") {
                useVersion(Version.KOTLIN)
            }
            if (requested.name.startsWith("junit-jupiter")) {
                useVersion("5.9.2")
            }
            if (requested.name == "slf4j-api") {
                useVersion("2.0.5")
            }
        }
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
