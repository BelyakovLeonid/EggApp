plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    kotlin("kapt")
    kotlin("android")
}


android {
    compileSdkVersion(AppConfig.compileSdk)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        applicationId = "leo.apps.eggy"
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
    implementation(Dependencies.viewBindingDelegate)
    implementation(Dependencies.Tests.junit)
    implementation(Dependencies.Tests.androidJunit)
    implementation(Dependencies.Tests.espresso)
    implementation(Dependencies.Jetpack.core)
    implementation(Dependencies.Jetpack.fragment)
    implementation(Dependencies.Ui.constraint)
    implementation(Dependencies.Ui.material)
    implementation(Dependencies.Ui.konfetti)
    implementation(Dependencies.Navigation.navigation)
    implementation(Dependencies.Dagger.dagger)
    kapt(Dependencies.Dagger.daggerCompiler)
}