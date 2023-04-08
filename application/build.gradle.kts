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

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
