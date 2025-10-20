plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
}
android {
    namespace = "com.module.wallet"
    compileSdk = COMPILE_SDK
    defaultConfig {
        minSdk = MIN_SDK
    }


    compileOptions {
        sourceCompatibility = SOURCE_COMPATIBILITY
        targetCompatibility = TARGET_COMPATIBILITY
    }
    kotlinOptions {
        jvmTarget = JVM_TARGET
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":module_basic"))
    implementation("com.android.billingclient:billing:7.0.0")
    implementation("com.android.billingclient:billing-ktx:7.0.0")
}