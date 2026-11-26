// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.android.legacy-kapt") version "9.5.2" apply false
    id("com.google.devtools.ksp") version "2.3.12" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.10.2" apply false
}