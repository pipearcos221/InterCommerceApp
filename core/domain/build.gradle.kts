plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Inyección de dependencias (JSR-330) para constructor injection en Domain
    compileOnly(libs.javax.inject)

    // Kotlin Coroutines para el manejo de Flows y procesos asíncronos en UseCases
    implementation(libs.kotlinx.coroutines.core)

    // Unit Testing
    testImplementation(libs.junit)
}
