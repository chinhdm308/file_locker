plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    `android-library`
//    `kotlin-android`
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

apply<MainGradlePlugin>()

android {
    namespace = "com.base.data"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))

    androidxCoreDependencies(hasConstraintLayout = false, hasMaterial = false, hasAppCompat = false)
    androidTestsDependencies()

    moshiKotlin()
    hiltDependencies()
    networkDependencies()
    roomDependencies()

    // Preferences DataStore
    datastorePreferences()

    timber()
}