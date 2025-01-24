import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

kotlin {
    jvmToolchain(17)

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata")
            dependencies {
                implementation(projects.core.config)
                implementation(projects.core.model)

                api(libs.room.runtime)
                api(libs.room.paging)
                api(libs.sqlite.bundled)

                implementation(libs.serialization)
                implementation(libs.kotlinx.datetime)
            }
        }
        commonTest.dependencies {
            api(libs.kotlin.test)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.apps.database"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lint {
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

