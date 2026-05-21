plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Kotlin Coroutines for Flow
    implementation(libs.kotlinx.coroutines.core)

    // For Unit tests
    testImplementation(libs.junit)
}
