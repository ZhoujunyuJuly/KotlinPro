import com.android.ide.common.symbols.getPackageNameFromManifest

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.origincode"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.origincode"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions{
            annotationProcessorOptions{
                arguments["module"] = project.name
                arguments["packageNameForAPT"] = rootProject.extra["packageNameForAPT"] as String
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":arouter-annotation"))

    //帮助我们通过类调用的形式类生成Java代码[JavaPoet]
    implementation("com.squareup:javapoet:1.9.0")

    //背后的服务，能够监听你是否在编译中
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    compileOnly("com.google.auto:auto-common:0.11")

    //它会告诉 Gradle：这个依赖里包含了一个注解处理器（实现了 AbstractProcessor）
    //这些处理器在 编译期 被调用，不会参与运行时逻辑。
    //实际上只要注解文件 AbstractProcessor 包含 SPI 文件，即 META_INF.services 就会被Gradle自动识别为注解文件
    annotationProcessor(project(":arouter-annotation"))
}