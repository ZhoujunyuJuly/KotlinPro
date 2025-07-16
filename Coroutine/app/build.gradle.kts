plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs)
}


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
        javaCompileOptions{
            kapt {
                arguments {
                    arg("room.schemaLocation", "$projectDir/schemas")
                }
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    dataBinding {
        enable = true
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
        aidl = true
    }
}

//‼️排除一个传递依赖项
//configurations.implementation{
//    exclude(group = "com.intellij", module = "annotations")
//}

dependencies {

    implementation(project(":server"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // 或最新版本
    testImplementation("junit:junit:4.13.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.rxjava3)
    implementation(libs.rxandroid)

    implementation(libs.kotlin.reflect)
    implementation(libs.mockwebserver)
    implementation(libs.moshi.kotlin)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.core)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.extensions)
    implementation(libs.androidx.databinding)
    //implementation(libs.google.dagger)
    implementation(libs.hilt)

    implementation(libs.okhttp.logging)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.common)
    implementation(libs.coil)
    implementation(libs.swipe.refreshlayout)
    implementation(libs.glide)
    implementation(libs.work.runtime)

    kapt(libs.hilt.compiler)
    kapt(libs.androidx.room.compiler)


    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment)
//    implementation(libs.androidx.navigation.safe.args)




    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}