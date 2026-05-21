pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "InterCommerceApp"
include(":app")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:database")
include(":core:ui")
include(":feature:catalog")
include(":feature:product_detail")
include(":feature:cart")
