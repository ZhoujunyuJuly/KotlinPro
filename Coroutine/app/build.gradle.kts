plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //lias(libs.plugins.hilt)
}

apply(plugin = "org.jetbrains.kotlin.kapt")

android {
    namespace = "com.example.coroutine"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.coroutine"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    dataBinding{
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // 或最新版本
    testImplementation("junit:junit:4.13.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.kotlin.reflect)
    implementation(libs.mockwebserver)
    implementation(libs.moshi.kotlin)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata.core)
    implementation(libs.lifecycle.livedata)
    //implementation(libs.google.dagger)
    //implementation(libs.hilt.core)
    //kapt(libs.hilt.compiler)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}