import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
//    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.base.presentation"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.chinchin.filelocker"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keystoreProperties = readProperties(rootProject.file("keystore.properties"))

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }

    viewBinding { enable = true }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":photoview"))
    implementation(project(":patternlockview"))

    activityDependencies()

    androidxCoreDependencies()
    androidTestsDependencies()

    hiltDependencies()
    glideDependencies()
    kotlinxCoroutinesAndroidDependencies()
    fragmentKtxDependencies()
    lifecycleExtensionsDependencies()
    lifecycleRuntimeDependencies()
    lifecycleCompiler()
    exifInterfaceDependencies()

    androidResponsiveSizeDependencies()
    timber()
    ratingDialogDependencies()
    implementation(Dependencies.retrofit)
    implementation("com.nostra13.universalimageloader:universal-image-loader:1.9.5")
    lottieDependencies()

    implementation(files("libs/fingerprintidentify_1.2.6.aar"))
    implementation("com.github.zcweng:switch-button:0.0.3@aar")
    implementation("io.github.shashank02051997:FancyToast:2.0.2")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

fun readProperties(propertiesFile: File) = Properties().apply {
    load(FileInputStream(propertiesFile))
}