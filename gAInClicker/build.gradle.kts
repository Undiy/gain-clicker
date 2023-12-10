// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("io.realm.kotlin") version "1.11.0" apply false
}

buildscript {
    extra.apply {
        set("lifecycle_version", "2.7.0-rc01")
        set("libres_version", "1.2.2")
        set("koin_version", "3.4.0")
    }
    dependencies {
        classpath("io.github.skeptick.libres:gradle-plugin:${rootProject.extra["libres_version"]}")
    }
}
