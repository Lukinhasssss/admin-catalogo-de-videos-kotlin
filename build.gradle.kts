import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("jacoco")
    id("org.sonarqube") version "3.4.0.2513"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.22.0-RC2"
}

group = "com.lukinhasssss.admin.catalogo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

// START OF DETEKT CONFIGURATION
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

val reportMerge by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.buildDir.resolve("reports/detekt/merge.xml")) // or "reports/detekt/merge.sarif"
    // output.set(rootProject.buildDir.resolve("reports/detekt/merge.html"))
}

subprojects {
    plugins.withType(DetektPlugin::class) {
        tasks.withType(Detekt::class) detekt@{
            finalizedBy(reportMerge)

            reportMerge.configure {
                input.from(this@detekt.xmlReportFile) // or .sarifReportFile
                // input.from(this@detekt.htmlReportFile)
            }
        }
    }
}
// END OF DETEKT CONFIGURATION

// START OF SONAR MULTI-MODULE CONFIGURATION
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

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.named("codeCoverageReport"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}