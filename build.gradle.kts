import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    jacoco
    id("org.sonarqube") version "3.4.0.2513"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
}

group = "com.lukinhasssss.admin.catalogo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
}

detekt {
    toolVersion = "1.21.0"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        sarif.required.set(false)
        txt.required.set(false)
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

apply(from = "${project.rootDir}/sonar.gradle.kts")

sonarqube {
    properties {
        property("sonar.projectKey", "")
        property("sonar.projectName", "")
        property("sonar.organization", "lukinhasssss")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir.parentFile.path}/build/reports/jacoco/codeCoverageReport/codeCoverageReport.xml")
        property("sonar.exclusions", "build/generated/**/*")
    }
}

subprojects {
    sonarqube {
        properties {
            property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir.parentFile.path}/build/reports/jacoco/codeCoverageReport/codeCoverageReport.xml")
        }
    }
}

// tasks.jacocoTestReport {
//     subprojects {
//         val project = this
//         project.plugins.withType(JacocoPlugin::class).configureEach {
//             project.tasks.matching { it.name == JacocoTaskExtension::class.qualifiedName }.configureEach {
//                 val testTask = this
//
//                 if (testTask.extensions.getByType(JacocoTaskExtension::class).isEnabled) {
//                     sourceSets(project.sourceSets.main.get())
//                     executionData(testTask)
//                 } else {
//                     logger.warn("Jacoco extension is disabled for test task '${testTask.name}' in project '${project.name}'. this test task will be excluded from jacoco report.")
//                 }
//             }
//         }
//     }
// }

// tasks.register("codeCoverageReport", JacocoReport::class) {
//     subprojects {
//         val project = this
//         logger.warn(project.name)
//         project.plugins.withType(JacocoPlugin::class).configureEach {
//             project.tasks.matching { logger.warn(it.name); it.name == JacocoTaskExtension::class.qualifiedName }.configureEach {
//                 val testTask = this
//
//                 if (testTask.extensions.getByType(JacocoTaskExtension::class).isEnabled) {
//                     sourceSets(project.sourceSets.main.get())
//                     executionData(testTask)
//                 } else {
//                     logger.warn("Jacoco extension is disabled for test task '${testTask.name}' in project '${project.name}'. this test task will be excluded from jacoco report.")
//                 }
//             }
//         }
//     }
// }