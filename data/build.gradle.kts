plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
    id("kotlin-kapt")
}

android {
    namespace = "com.base.data"
    compileSdk = project.libs.versions.compileSDKVersion.get().toInt()

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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    kotlinOptions {
        jvmTarget = "18"
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.coreKtx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.testJunit)
    androidTestImplementation(libs.testEspressoCore)

    implementation(libs.moshiKotlin)
    ksp(libs.moshiKotlinCodegen)

    implementation(libs.hiltAndroid)
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.retrofit)
    implementation(libs.converterMoshi)

    // define a BOM and its version
    implementation(platform(libs.okhttpBom))
    // define any required OkHttp artifacts without version
    implementation(libs.bundles.okhttp)

    implementation(libs.roomKtx)
    implementation(libs.roomRuntime)
    ksp(libs.roomCompiler)

    implementation(libs.datastorePreferences)
    implementation(libs.datastorePreferencesCore)

    implementation(libs.timber)
}