plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("kapt")
    kotlin("android")
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        applicationId = "leo.apps.eggy"
        compileSdk = AppConfig.compileSdk
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = 3
        versionName = "1.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
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
    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.analytics)
    implementation(Dependencies.Firebase.crashlytics)
}