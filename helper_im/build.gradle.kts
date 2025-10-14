plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "com.helper.im"
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(project(":helper_develop"))
    api("com.netease.nimlib:basesdk:10.9.30")
// 聊天室功能
    api("com.netease.nimlib:chatroom:10.9.30")
// 厂商推送集成（小米、华为等）
    api("com.netease.nimlib:push:10.9.30")
// 超大群功能
    api("com.netease.nimlib:superteam:10.9.30")
// 全文检索插件
    api("com.netease.nimlib:lucene:10.9.30")
    //音频
    api("com.netease.yunxin:nertc-audio:5.6.50")
    api("com.netease.yunxin:nertc-audio:5.6.50")
}