plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    kotlin("kapt")
    kotlin("android")
    kotlin("android.extensions")
}


android {
    compileSdkVersion(AppConfig.compileSdk)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        applicationId = "com.example.eggyapp"
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(Dependencies.kotlin)
    implementation(Dependencies.singleLiveEvent)
    implementation(Dependencies.Tests.junit)
    implementation(Dependencies.Tests.androidJunit)
    implementation(Dependencies.Tests.espresso)
    implementation(Dependencies.Jetpack.core)
    implementation(Dependencies.Jetpack.fragment)
    implementation(Dependencies.Ui.constraint)
    implementation(Dependencies.Ui.material)
    implementation(Dependencies.Ui.konfetti)
    implementation(Dependencies.Navigation.navigation)
    implementation(Dependencies.Navigation.navigationUi)
    implementation(Dependencies.RxJava.rxJava)
    implementation(Dependencies.RxJava.rxJavaAndroid)
    implementation(Dependencies.Dagger.dagger)
    implementation(Dependencies.Dagger.daggerAndroid)
    kapt(Dependencies.Dagger.daggerCompiler)
    kapt(Dependencies.Dagger.daggerProcessor)
}