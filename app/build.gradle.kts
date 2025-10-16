import java.util.Locale

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
//    alias(libs.plugins.google.services)
}
android {
    namespace = NAMESPACE
    compileSdk = COMPILE_SDK
    defaultConfig {
        targetSdk = TARGET_SDK
        minSdk = MIN_SDK
        ndk {
            //设置支持的 SO 库架构
            abiFilters.addAll(listOf("armeabi-v7a", "x86", "arm64-v8a", "x86_64"))
        }
    }

    signingConfigs {
        ProductFlavors.all.forEach {
            create(it.signingConfigs.name) {
                storeFile = file(it.signingConfigs.storeFile)
                storePassword = it.signingConfigs.storePassword
                keyAlias = it.signingConfigs.keyAlias
                keyPassword = it.signingConfigs.keyPassword
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            isMinifyEnabled = false
        }
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
        buildConfig = true
    }
    flavorDimensions += listOf("default")
    productFlavors {
        ProductFlavors.all.forEach {
            create(it.name) {
                dimension = "default"
                applicationId = it.applicationId
                versionCode = it.versionCode
                versionName = it.versionName
                signingConfig = signingConfigs.getByName(it.signingConfigs.name)
                setProperty("archivesBaseName", "layaliya_${ it.versionName}")
            }
        }
    }
}

dependencies {
    implementation(project(":module_basic"))
    implementation(project(":module_room"))
    implementation(project(":module_mine"))
    implementation(project(":module_chat"))
    implementation(project(":module_chatroom"))
    implementation(project(":module_community"))
    implementation(project(":module_login"))
    implementation(project(":module_noble"))
    implementation(project(":module_agent"))
    implementation(project(":module_comment"))
    implementation(project(":module_gift"))
    implementation(project(":module_emoji"))
    implementation(project(":module_wallet"))
    implementation(project(":module_charm"))
    implementation(project(":module_wealth"))
    implementation(project(":module_store"))
    implementation(project(":module_setting"))
    implementation(project(":module_bag"))
    implementation(project(":helper_im"))
    implementation("com.android.billingclient:billing:6.1.0")
}

tasks.register("buildOnline") {
    group = "custom"
    description = "Build release APKs and move immediately after each build"
    val outputDir = file("${project(":app").projectDir}/apks/online")
    // 初始化目录
    outputDir.deleteRecursively()
    outputDir.mkdirs()

        val assembleTask = tasks.getByName("assembleOnlineRelease")
        dependsOn(assembleTask)
        // 为每个渠道任务添加移动动作
        assembleTask.doLast {
            val apkFiles = fileTree("${layout.buildDirectory.get()}/outputs/apk") {
                include("**/Online/release/*-release.apk")
                exclude("**/*-unaligned.apk")
            }

            apkFiles.forEach { apkFile ->
                copy {
                    from(apkFile)
                    into(outputDir)
                    rename { apkFile.name }
                }
                println("[online] APK已移动至: ${outputDir.absolutePath}/${apkFile.name}")
            }
        }

    doLast {
        println("所有渠道包已构建并移动完成")
        println("最终目录内容:")
        outputDir.listFiles()?.forEach { println("  - ${it.name}") }
    }
}
