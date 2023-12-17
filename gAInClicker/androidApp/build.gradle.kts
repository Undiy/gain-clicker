plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {

                implementation(project(":shared"))

//                api("io.insert-koin:koin-compose:1.0.4")
            }
        }
    }
}

android {
    namespace = "com.example.android.gainclicker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.android.gainclicker"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//
//    implementation(libs.androidx.core.ktx)

//    implementation(libs.androidx.lifecycle.viewModelCompose)
//    implementation(libs.androidx.lifecycle.runtimeCompose)
//
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.compose.ui)
//    implementation(libs.androidx.compose.material3)
//
//    implementation(libs.androidx.datastore.preferences.core)
//
//    implementation(libs.realm.base)
//
//    testImplementation(libs.junit4)
//
//    androidTestImplementation(libs.androidx.test.ext)
//    androidTestImplementation(libs.androidx.test.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.compose.ui.test)
//
//
}
