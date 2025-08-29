// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.github.ben.manes)
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}



tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}