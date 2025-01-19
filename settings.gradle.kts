rootProject.name = "Persona"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(
    ":androidApp",
    ":shared",
    ":core:config",
    ":core:database",
    ":core:designsystem",
    ":core:model",
    ":feature:usergen",
    ":library:paging",
)