import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.mokkery)
}

kotlin {
    jvmToolchain(17)

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0"
        summary = "User Generator"
        homepage = "User Generator"
        framework {
            baseName = "usergen"
            isStatic = true
        }
        podfile = project.file("../../iosApp/Podfile")
    }

    sourceSets {
        androidUnitTest.dependencies {
            implementation(libs.robolectric)
            implementation(libs.koin.test.junit4)
        }
        commonMain.dependencies {
            implementation(projects.core.config)
            implementation(projects.core.database)
            implementation(projects.core.designsystem)
            implementation(projects.core.model)
            implementation(projects.core.network)
            implementation(projects.library.paging)
            implementation(projects.library.placeholder)

            implementation(libs.navigation.compose)

            implementation(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
            implementation(libs.serialization)
            implementation(libs.androidx.paging.common)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)

            implementation(libs.koin.test)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.ktor.mock)

            implementation(libs.androidx.paging.testing)
        }
    }
}

android {
    namespace = "com.apps.usergen"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    lint {
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    androidTestImplementation(libs.android.compose.ui.junit)
    debugImplementation(libs.android.compose.ui.test)

    testImplementation(libs.android.compose.ui.junit)
    testImplementation(libs.test.runner)
    debugImplementation(libs.android.compose.ui.test)
    androidTestUtil(libs.test.orchestrator)
}

compose.resources {
    packageOfResClass = "com.apps.usergen"
    generateResClass = always
    publicResClass = false
}