import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

plugins {
    kotlin("jvm") version Version.kotlin
    id("org.sonarqube") version Version.sonarqube
    id("org.jlleitschuh.gradle.ktlint") version Version.ktlint
    id("io.gitlab.arturbosch.detekt") version Version.detekt
}

group = "com.lukinhasssss.admin.catalogo"

repositories {
    mavenCentral()
}

// START OF DETEKT AND KTLINT CONFIGURATION
detekt {
    toolVersion = Version.detekt
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

val detektReportMerge by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.buildDir.resolve("reports/detekt/detekt-report.xml"))
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    plugins.withType(DetektPlugin::class) {
        tasks.withType(Detekt::class) detekt@{
            finalizedBy(detektReportMerge)

            detektReportMerge.configure {
                input.from(this@detekt.xmlReportFile)
            }
        }
    }
}
// END OF DETEKT AND KTLINT CONFIGURATION

// START OF SONAR MULTI-MODULE CONFIGURATION
sonar {
    properties {
        property("sonar.projectKey", "Lukinhasssss_admin-catalogo-de-videos-kotlin")
        property("sonar.projectName", "admin-catalogo-de-videos-kotlin")
        property("sonar.organization", "lukinhasssss")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${projectDir.parentFile.path}/build/reports/jacoco/test/jacocoTestReport.xml"
        )
        property("sonar.exclusions", "build/generated/**/*")
    }
}

subprojects {
    sonar {
        properties {
            property(
                "sonar.coverage.jacoco.xmlReportPaths",
                "${projectDir.parentFile.path}/build/reports/jacoco/test/jacocoTestReport.xml"
            )
        }
    }
}
// END OF SONAR MULTI-MODULE CONFIGURATION

/*** SOMENTE PARA APRENDIZADO ***/

// START OF JACOCO MULTI-PROJECT CONFIGURATION
// tasks.register<JacocoReport>("codeCoverageReport") {
//     subprojects {
//         this@subprojects.plugins.withType<JacocoPlugin>().configureEach {
//             this@subprojects.tasks.matching {
//                 it.extensions.findByType<JacocoTaskExtension>() != null
//             }.configureEach {
//                 sourceSets(this@subprojects.the<SourceSetContainer>().named("main").get())
//                 executionData(this)
//             }
//         }
//     }
//
//     reports {
//         xml.required.set(true)
//         html.required.set(true)
//     }
// }
// END OF JACOCO MULTI-PROJECT CONFIGURATION

// tasks.withType<KotlinCompile> {
//     kotlinOptions {
//         useK2 = false
//         javaParameters = true
//         jvmTarget = JavaVersion.VERSION_17.toString()
//         freeCompilerArgs = listOf("-Xjsr305=strict")
//     }
// }
//
// tasks.withType<Test> {
//     useJUnitPlatform()
// }
//
// jacoco {
//     toolVersion = "0.8.8"
// }
//
// tasks.register<Test>("unitTests") {
//     description = "Runs unit tests"
//     group = "verification"
//
//     useJUnitPlatform {
//         includeTags("unitTest")
//     }
//
//     shouldRunAfter(tasks.test)
// }
//
// tasks.register<Test>("integrationTests") {
//     description = "Runs integration tests"
//     group = "verification"
//
//     useJUnitPlatform {
//         includeTags("integrationTest")
//     }
//
//     shouldRunAfter(tasks.test)
// }
//
// tasks.register<Test>("e2eTests") {
//     description = "Runs e2e tests"
//     group = "verification"
//
//     useJUnitPlatform {
//         includeTags("e2eTest")
//     }
//
//     shouldRunAfter(tasks.test)
// }
