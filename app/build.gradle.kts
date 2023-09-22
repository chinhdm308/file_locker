import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
//    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    kotlin("kapt")

}

android {
    namespace = "com.base.basemvvmcleanarchitecture"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.base.basemvvmcleanarchitecture"
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

    implementation("com.wei.android.lib:fingerprintidentify:1.2.6")
//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
//    implementation(files("src/main/libs/fingerprintidentify_1.2.6.aar"))
//    implementation(linkedMapOf("name" to "fingerprintidentify_1.2.6","ext" to "aar"))
//    implementation(files("libs/fingerprintidentify_1.2.6.aar"))
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

fun readProperties(propertiesFile: File) = Properties().apply {
    load(FileInputStream(propertiesFile))
}