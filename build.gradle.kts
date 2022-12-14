import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    id("jacoco")
    id("org.sonarqube") version "3.5.0.2730"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0-RC2"
}

group = "com.lukinhasssss.admin.catalogo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// START OF DETEKT AND KTLINT CONFIGURATION
detekt {
    toolVersion = "1.22.0-RC2"
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
            "${projectDir.parentFile.path}/build/reports/jacoco/codeCoverageReport/codeCoverageReport.xml"
        )
        property("sonar.exclusions", "build/generated/**/*")
    }
}

subprojects {
    sonar {
        properties {
            property(
                "sonar.coverage.jacoco.xmlReportPaths",
                "${projectDir.parentFile.path}/build/reports/jacoco/codeCoverageReport/codeCoverageReport.xml"
            )
        }
    }
}
// END OF SONAR MULTI-MODULE CONFIGURATION

// START OF JACOCO MULTI-PROJECT CONFIGURATION
tasks.register<JacocoReport>("codeCoverageReport") {
    subprojects {
        this@subprojects.plugins.withType<JacocoPlugin>().configureEach {
            this@subprojects.tasks.matching {
                it.extensions.findByType<JacocoTaskExtension>() != null
            }.configureEach {
                sourceSets(this@subprojects.the<SourceSetContainer>().named("main").get())
                executionData(this)
            }
        }
    }

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
// END OF JACOCO MULTI-PROJECT CONFIGURATION

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        useK2 = false
        javaParameters = true
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
