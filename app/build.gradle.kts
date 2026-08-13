import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "2.4.10"
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget("17")
        freeCompilerArgs = listOf("-Xjvm-default=all-compatibility", "-opt-in=kotlin.RequiresOptIn")
    }
}

android {
    compileSdk = 36

    defaultConfig {
        applicationId = "com.gios.brightthumb"
        minSdk = 24
        targetSdk = 36
        // CI stamps versionCode from the run number so it always increases;
        // local builds fall back to 1.
        versionCode = (project.findProperty("versionCode") as String? ?: "1").toInt()
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ksp { arg("room.schemaLocation", "$projectDir/schemas") }
    }

    // Necessary for izzyondroid releases
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }

    // One stable key, committed, so every release (CI or local) is signed the same
    // way and Obtainium updates never break. Cert is pinned in signing-fingerprint.txt
    // and CI fails if it ever drifts. CI ships the debug variant, so the debug
    // signing config is overridden with the same key.
    signingConfigs {
        getByName("debug") {
            storeFile = file("../keystore/brightthumb.jks")
            storePassword = "brightthumb"
            keyAlias = "brightthumb"
            keyPassword = "brightthumb"
        }
        create("release") {
            storeFile = file("../keystore/brightthumb.jks")
            storePassword = "brightthumb"
            keyAlias = "brightthumb"
            keyPassword = "brightthumb"
            enableV1Signing = true
            enableV2Signing = true
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                // Includes the default ProGuard rules files that are packaged with
                // the Android Gradle plugin. To learn more, go to the section about
                // R8 configuration files.
                getDefaultProguardFile("proguard-android-optimize.txt"),

                // Includes a local, custom Proguard rules file
                "proguard-rules.pro"
            )
        }
        // CI ships the debug variant as the release (signed with the production
        // keystore), so no .debug suffix — the installed identity must be exactly
        // com.gios.brightthumb or updates break and the index mis-keys the app.
        debug {
        }
    }

    lint {
        disable += "MissingTranslation"
        disable += "KtxExtensionAvailable"
        disable += "UseKtx"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    namespace = "com.gios.brightthumb"
}

dependencies {
    // Freedroidwarn
    implementation("com.github.woheller69:FreeDroidWarn:V1.13")

    // Exporting / importing DB helper
    implementation("com.github.dessalines:room-db-export-import:0.1.1")

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2026.06.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.runtime:runtime-livedata:1.11.4")

    // Activities
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.activity:activity-ktx:1.13.0")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-runtime-compose")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.8")

    // Emoji Picker
    implementation("androidx.emoji2:emoji2-emojipicker:1.6.0")

    // Markdown
    implementation("com.github.jeziellago:compose-markdown:0.7.2")

    // Preferences
    implementation("me.zhanghai.compose.preference:library:1.1.1")

    // Input switcher
    implementation("com.louiscad.splitties:splitties-systemservices:3.0.0")
    implementation("com.louiscad.splitties:splitties-views:3.0.0")

    // Room
    // To use Kotlin annotation processing tool
    ksp("androidx.room:room-compiler:2.8.4")
    implementation("androidx.room:room-runtime:2.8.4")
    annotationProcessor("androidx.room:room-compiler:2.8.4")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.8.4")

    // App compat
    implementation("androidx.appcompat:appcompat:1.7.1")

    // YAML serialization
    implementation("com.charleskorn.kaml:kaml:0.104.0")

    // Kotlin Reflect
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.4.10")

    // Arrow-kt for mutating deeply nested data classes
    implementation("io.arrow-kt:arrow-optics:2.2.3")
    ksp("io.arrow-kt:arrow-optics-ksp-plugin:2.2.3")
}
