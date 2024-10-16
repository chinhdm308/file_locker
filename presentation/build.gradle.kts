import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hiltAndroid)
//    alias(libs.plugins.googleServices)
//    alias(libs.plugins.firebaseCrashlytics)
//    alias(libs.plugins.firebasePerf)
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.base.presentation"
    compileSdk = project.libs.versions.compileSDKVersion.get().toInt()

    defaultConfig {
        applicationId = "com.chinchin.filelocker"
        minSdk = project.libs.versions.minimumSDK.get().toInt()
        targetSdk = project.libs.versions.targetSDK.get().toInt()
        versionCode = 1
        versionName = "1.0.0"

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
            isMinifyEnabled = true
            isShrinkResources = true
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

    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.testJunit)
    androidTestImplementation(libs.testEspressoCore)

    //DI
    implementation(libs.hiltAndroid)
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.glide)
    ksp(libs.glideKsp)

    implementation(libs.activityKtx)
    implementation(libs.fragmentKtx)
    implementation(libs.lifecycleRuntime)
    implementation(libs.lifecycleExtensions)
    implementation(libs.lifecycleCompiler)

    implementation(libs.exifinterface)
    implementation(libs.bundles.multiScreenDesign)
    implementation(libs.retrofit)
    implementation(libs.jsoup)
    implementation(libs.timber)
    implementation(libs.lottie)
    implementation("com.nostra13.universalimageloader:universal-image-loader:1.9.5")
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