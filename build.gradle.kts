plugins {
    kotlin("jvm") version "2.0.21"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

dependencies {
    implementation("com.varabyte.kotter:kotter-jvm:1.2.1")
    testImplementation("com.varabyte.kotterx:kotter-test-support-jvm:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

tasks {
    wrapper {
        gradleVersion = "8.11"
    }
}
