plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.video"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.video"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild{
            cmake{
                cppFlags("")
            }
        }

        ndk{
            abiFilters += listOf("armeabi-v7a","x86_64","arm64-v8a")
        }
    }

    externalNativeBuild{
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}




dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}