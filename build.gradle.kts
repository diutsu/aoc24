plugins {
    kotlin("jvm") version "2.0.21"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11"
    }
}
