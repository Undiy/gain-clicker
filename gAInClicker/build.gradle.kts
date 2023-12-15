plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.gradlePlugin) apply false
    alias(libs.plugins.realm.gradlePlugin) apply false
}

buildscript {
    dependencies {
        classpath(libs.libres)
    }
}
