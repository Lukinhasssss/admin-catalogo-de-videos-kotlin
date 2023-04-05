plugins {
    java
    id("jacoco")
}

version = Version.PROJECT

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.vavr:vavr-kotlin:${Version.VAVR}")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = Version.JACOCO
}

tasks.register<Test>("unitTests") {
    description = "Runs unit tests"
    group = "verification"

    useJUnitPlatform {
        includeTags("unitTest")
    }

    shouldRunAfter(tasks.test)
}

tasks.register<Test>("integrationTests") {
    description = "Runs integration tests"
    group = "verification"

    useJUnitPlatform {
        includeTags("integrationTest")
    }

    shouldRunAfter(tasks.test)
}

tasks.register<Test>("e2eTests") {
    description = "Runs e2e tests"
    group = "verification"

    useJUnitPlatform {
        includeTags("e2eTest")
    }

    shouldRunAfter(tasks.test)
}
