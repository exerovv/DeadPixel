pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DeadPixel"
include(":app")

// Core modules
include(":core-network")
include(":core-ui")
include(":core-database")

// Feature modules
include(":feature-auth")
include(":feature-users")
include(":feature-equipment")
include(":feature-orders")
include(":feature-diagnostics")
include(":feature-notifications")
include(":feature-workplans")
include(":feature-reports")
