import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.exerovv.deadpixel.core.network"
    compileSdk = 36
    defaultConfig { minSdk = 30 }
    kotlin { compilerOptions { jvmTarget = JvmTarget.JVM_11 } }
}

dependencies {
    api(libs.retrofit)
    api(libs.converter.kotlinx.serialization)
    api(libs.kotlinx.serialization.json)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.core.ktx)
}
