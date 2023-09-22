plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    id("com.google.gms.google-services")
}


apply<MainGradlePlugin>()

android {
    namespace = "com.base.admob"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    viewBinding { enable = true }
}

dependencies {
    androidxCoreDependencies(hasConstraintLayout = false)
    androidTestsDependencies()

    playServicesAds()
    lifecycleRuntimeDependencies()
    lifecycleExtensionsDependencies()
}