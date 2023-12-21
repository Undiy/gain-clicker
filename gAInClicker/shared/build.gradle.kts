plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    id(libs.plugins.libres.gradlePlugin.get().pluginId)
    alias(libs.plugins.realm.gradlePlugin)

}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.realm.base)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.datastore.preferences.core)

                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.material3)

                api(libs.koin.core)
                api(libs.koin.compose)

                api(libs.precompose)
                api(libs.precompose.viewmodel)
                api(libs.precompose.koin)

                implementation(libs.kotlinx.datetime)

                api(libs.napier)
            }
        }

        val androidMain by getting {
            dependencies {
                api(libs.koin.android)
            }
        }
    }
}

android {
    namespace = "com.example.android.gainclicker.common"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

libres {
    generateNamedArguments = true
}
