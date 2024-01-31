import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.shadow)


}

kotlin {
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":shared"))

                implementation(compose.desktop.currentOs)
            }
        }
    }
}

fun registerShadowJar(targetName: String) {
    // Get the target casting to KotlinJvmTarget in the process
    kotlin.targets.named<KotlinJvmTarget>(targetName) {
        // Access the main compilation
        compilations.named("main") {
            tasks {
                val shadowJar = register<ShadowJar>("${targetName}ShadowJar") {
                    group = "build"
                    from(output)
                    configurations = listOf(runtimeDependencyFiles)
                    archiveBaseName = "gain-clicker"
                    archiveAppendix = targetName
                    archiveClassifier = "all"
                    manifest {
                        attributes(
                            "Main-Class" to "MainKt",
                            "Implementation-Title" to "gAIn Clicker",
                            "Implementation-Version" to libs.versions.appVersion.get(),
                            "Implementation-Vendor" to "yarutin.eu@gmail.com"
                        )
                    }
                    mergeServiceFiles()
                }
                getByName("${targetName}Jar") {
                    finalizedBy(shadowJar)
                }
            }
        }
    }
}

registerShadowJar("desktop")