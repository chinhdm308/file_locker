import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object Dependencies {
    const val playServiceAds = "com.google.android.gms:play-services-ads:${Versions.playServiceAds}"

    const val daggerAndroid = "com.google.dagger:dagger:${Versions.hilt}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.hilt}"

    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltAgp = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"

    // define a BOM and its version
    const val okHttpBom = "com.squareup.okhttp3:okhttp-bom:${Versions.okHttp}"

    // define any required OkHttp artifacts without version
    const val okHttp = "com.squareup.okhttp3:okhttp"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"

    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCommon = "androidx.room:room-common:${Versions.room}"

    //multiple screen - layout + textSize
    const val sdpAndroid = "com.intuit.sdp:sdp-android:${Versions.multipleScreen}"
    const val sspAndroid = "com.intuit.ssp:ssp-android:${Versions.multipleScreen}"

    //load image
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activity}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val preference = "androidx.preference:preference:${Versions.preference}"
    const val swipeToRefreshLayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeLayout}"
    const val splashscreen = "androidx.core:core-splashscreen:${Versions.splashscreen}"

    const val lifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    const val lifecycleRuntime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycleVersion}"

    const val kotlinxCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinxCoroutinesAndroid}"

    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navVersion}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navVersion}"

    const val playStoreCore = "com.google.android.play:core:${Versions.playStoreCore}"
    const val playStoreKtx = "com.google.android.play:core-ktx:${Versions.playStoreKtx}"

    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    const val firebaseCore = "com.google.firebase:firebase-core"
    const val firebaseMessaging = "com.google.firebase:firebase-messaging-ktx"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"

    const val ratingDialog = "com.codemybrainsout.rating:ratingdialog:${Versions.ratingDialog}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
    const val exifInterface = "androidx.exifinterface:exifinterface:${Versions.exifInterface}"
    const val leakcanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val circleImageview = "de.hdodenhof:circleimageview:${Versions.circleImageView}"
    const val dotIndicator = "com.tbuonomo:dotsindicator:${Versions.dotsIndicator}"
    const val alerter = "com.github.tapadoo:alerter:${Versions.alerter}"
    const val imageZoomView = "com.github.chrisbanes:PhotoView:${Versions.imageZoom}"
    const val epoxyCore = "com.airbnb.android:epoxy:${Versions.epoxy}"

    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshiKotlin}"
    const val moshiKotlinCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiKotlin}"

    const val datastorePreferences =
        "androidx.datastore:datastore-preferences:${Versions.datastorePreferences}"
    const val datastorePreferencesCore =
        "androidx.datastore:datastore-preferences-core:${Versions.datastorePreferences}"

    const val playServicesAds =
        "com.google.android.gms:play-services-ads:${Versions.playServicesAds}"

    const val junit = "junit:junit:${Versions.junit}"
    const val extJunit = "androidx.test.ext:junit:${Versions.extJunit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}

fun DependencyHandler.photoviewModule() {
    implementation(project(":photoview"))
}

fun DependencyHandler.playServicesAds() {
    implementation(Dependencies.playServiceAds)
}

fun DependencyHandler.daggerAndroid() {
    implementation(Dependencies.daggerAndroid)
    kapt(Dependencies.daggerCompiler)
}

fun DependencyHandler.datastorePreferences() {
    implementation(Dependencies.datastorePreferences)
    implementation(Dependencies.datastorePreferencesCore)
}

fun DependencyHandler.moshiKotlin() {
    implementation(Dependencies.moshiKotlin)
    kapt(Dependencies.moshiKotlinCodegen)
    //    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
}

fun DependencyHandler.timber() {
    implementation(Dependencies.timber)
}

fun DependencyHandler.androidTestsDependencies() {
    implementation(Dependencies.junit)
    implementation(Dependencies.extJunit)
    implementation(Dependencies.espressoCore)
}

fun DependencyHandler.androidxCoreDependencies(
    hasMaterial: Boolean = true,
    hasAppCompat: Boolean = true,
    hasConstraintLayout: Boolean = true
) {
    implementation(Dependencies.coreKtx)
    if (hasAppCompat) implementation(Dependencies.appcompat)
    if (hasMaterial) implementation(Dependencies.material)
    if (hasConstraintLayout) implementation(Dependencies.constraintLayout)
}

fun DependencyHandler.imageZoomViewDependencies() {
    implementation(Dependencies.imageZoomView)
}

fun DependencyHandler.playCoreDependencies() {
    implementation(Dependencies.playStoreCore)
    implementation(Dependencies.playStoreKtx)
}

fun DependencyHandler.firebaseDependencies() {
    implementation(platform(Dependencies.firebaseBom))
    implementation(Dependencies.firebaseCore)
    implementation(Dependencies.firebaseMessaging)
    implementation(Dependencies.firebaseAnalytics)
    implementation(Dependencies.firebaseCrashlytics)
}

fun DependencyHandler.androidxNavigationDependencies() {
    implementation(Dependencies.navigationFragmentKtx)
    implementation(Dependencies.navigationUiKtx)
}

fun DependencyHandler.androidxSwipeRefreshLayoutDependencies() {
    implementation(Dependencies.swipeToRefreshLayout)
}

fun DependencyHandler.kotlinxCoroutinesAndroidDependencies() {
    implementation(Dependencies.kotlinxCoroutinesAndroid)
}

fun DependencyHandler.splashscreenDependencies() {
    implementation(Dependencies.splashscreen)
}

fun DependencyHandler.playServicesAdsDependencies() {
    implementation(Dependencies.playServicesAds)
}

fun DependencyHandler.lifecycleExtensionsDependencies() {
    implementation(Dependencies.lifecycleExtensions)
}

fun DependencyHandler.exifInterfaceDependencies() {
    implementation(Dependencies.exifInterface)
}

fun DependencyHandler.lottieDependencies() {
    implementation(Dependencies.lottie)
}

fun DependencyHandler.ratingDialogDependencies() {
    implementation(Dependencies.ratingDialog)
}

fun DependencyHandler.lifecycleRuntimeDependencies() {
    implementation(Dependencies.lifecycleRuntime)
}

fun DependencyHandler.fragmentKtxDependencies() {
    implementation(Dependencies.fragmentKtx)
}

fun DependencyHandler.glideDependencies() {
    implementation(Dependencies.glide)
    kapt(Dependencies.glideCompiler)
}

fun DependencyHandler.androidResponsiveSizeDependencies() {
    implementation(Dependencies.sdpAndroid)
    implementation(Dependencies.sspAndroid)
}

fun DependencyHandler.roomDependencies() {
    implementation(Dependencies.roomRuntime)
    implementation(Dependencies.roomKtx)
    kapt(Dependencies.roomCompiler)
}

fun DependencyHandler.networkDependencies() {
    implementation(Dependencies.retrofit)
    implementation(Dependencies.moshiConverter)
    implementation(platform(Dependencies.okHttpBom))
    implementation(Dependencies.okHttp)
    implementation(Dependencies.okHttpLoggingInterceptor)
}

fun DependencyHandler.hiltDependencies() {
    implementation(Dependencies.hiltAndroid)
    kapt(Dependencies.hiltCompiler)
}

fun DependencyHandler.lifecycleCompiler() {
    implementation(Dependencies.lifecycleCompiler)
}

fun DependencyHandler.activityDependencies() {
    implementation(Dependencies.activityKtx)
}