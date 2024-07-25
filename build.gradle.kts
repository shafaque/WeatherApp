// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.dokka.gradle) apply false
    alias(libs.plugins.hilt.gradle) apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.22" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {

        classpath(libs.hilt.android.gradle.plugin)
    }
}
