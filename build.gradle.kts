plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt)
    jacoco
}

val detektVersion = libs.versions.detekt.get()
val jacocoVersion = libs.versions.jacoco.get()

detekt {
    toolVersion = detektVersion
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
    autoCorrect = true
}

val jacocoTestReportExcludes = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    "**/androidx/**/*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MemberInjector*.*",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*_ViewBinding*.*",
    "**/AutoValue_*.*",
    "**/R2.class",
    "**/R2$*.class",
    "**/*Directions$*",
    "**/*Directions.*",
    "**/*Args$*",
    "**/*Args.*",
    "**/BR.class",
    "**/DataBinderMapperImpl*.*",
    "**/DataBindingInfo.class",
    "**/*_Impl*.*"
)

subprojects {
    apply(plugin = "jacoco")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    }

    configure<JacocoPluginExtension> {
        toolVersion = jacocoVersion
    }

    tasks.withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

    afterEvaluate {
        val isAndroidModule = plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")
        val isKotlinModule = plugins.hasPlugin("org.jetbrains.kotlin.jvm") || plugins.hasPlugin("org.jetbrains.kotlin.android")
        
        if (isAndroidModule || isKotlinModule) {
            val jacocoTask = if (tasks.findByName("jacocoTestReport") == null) {
                tasks.register<JacocoReport>("jacocoTestReport")
            } else {
                tasks.named<JacocoReport>("jacocoTestReport")
            }

            jacocoTask.configure {
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                }

                if (isAndroidModule) {
                    dependsOn("testDebugUnitTest")
                    val debugTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
                        exclude(jacocoTestReportExcludes)
                    }
                    val mainSrc = "${project.projectDir}/src/main/java"
                    val kotlinSrc = "${project.projectDir}/src/main/kotlin"

                    sourceDirectories.setFrom(files(mainSrc, kotlinSrc))
                    classDirectories.setFrom(files(debugTree))
                    executionData.setFrom(fileTree(layout.buildDirectory) {
                        include("jacoco/testDebugUnitTest.exec")
                    })
                } else {
                    if (tasks.findByName("test") != null) {
                        dependsOn("test")
                        val classTree = fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
                            exclude(jacocoTestReportExcludes)
                        }
                        val mainSrc = "${project.projectDir}/src/main/kotlin"

                        sourceDirectories.setFrom(files(mainSrc))
                        classDirectories.setFrom(files(classTree))
                        executionData.setFrom(fileTree(layout.buildDirectory) {
                            include("jacoco/test.exec")
                        })
                    }
                }
            }
        }
    }
}

tasks.register<JacocoReport>("jacocoFullReport") {
    val subprojectsWithJacoco = subprojects.filter { subproject ->
        subproject.tasks.findByName("jacocoTestReport") != null
    }
    
    dependsOn(subprojectsWithJacoco.map { it.tasks.named("jacocoTestReport") })

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val sourceDirs = subprojectsWithJacoco.flatMap { listOf(it.layout.projectDirectory.dir("src/main/java"), it.layout.projectDirectory.dir("src/main/kotlin")) }
    
    val classDirs = subprojectsWithJacoco.map { subproject ->
        val isAndroid = subproject.plugins.hasPlugin("com.android.application") || subproject.plugins.hasPlugin("com.android.library")
        if (isAndroid) {
            subproject.fileTree(subproject.layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
                exclude(jacocoTestReportExcludes)
            }
        } else {
            subproject.fileTree(subproject.layout.buildDirectory.dir("classes/kotlin/main")) {
                exclude(jacocoTestReportExcludes)
            }
        }
    }

    val execData = subprojectsWithJacoco.map { 
        it.fileTree(it.layout.buildDirectory) {
            include("jacoco/testDebugUnitTest.exec", "jacoco/test.exec")
        }
    }

    sourceDirectories.setFrom(files(sourceDirs))
    classDirectories.setFrom(files(classDirs))
    executionData.setFrom(files(execData))
}
