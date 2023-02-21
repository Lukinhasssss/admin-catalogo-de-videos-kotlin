plugins {
    java
    id("jacoco")
}

version = Version.project

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = Version.jacoco
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
