plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    // Inyección de dependencias (JSR-330) para constructor injection en Domain
    compileOnly(libs.javax.inject)

    // Kotlin Coroutines para el manejo de Flows y procesos asíncronos en UseCases
    implementation(libs.kotlinx.coroutines.core)
    
    // Paging Common (Pure Kotlin)
    implementation(libs.androidx.paging.common)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.paging.testing)
}
