object Dependencies {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val viewBindingDelegate =
        "com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Versions.viewBindingDelegate}"

    object TopLevel {
        const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val safeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    }

    object Tests {
        const val junit = "junit:junit:${Versions.junit}"
        const val androidJunit = "androidx.test.ext:junit:${Versions.androidJunit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    object Jetpack {
        const val core = "androidx.core:core-ktx:${Versions.jetpackCore}"
        const val fragment = "androidx.fragment:fragment-ktx:${Versions.jetpackFragment}"
    }

    object Ui {
        const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
        const val material = "com.google.android.material:material:${Versions.material}"
        const val konfetti = "nl.dionsegijn:konfetti:${Versions.konfetti}"
    }

    object Navigation {
        const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    }

    object Dagger {
        const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }

    object Firebase {
        const val core = "com.google.gms:google-services:${Versions.Firebase.core}"
        const val bom = "com.google.firebase:firebase-bom:${Versions.Firebase.bom}"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlyticsGradle = "com.google.firebase:firebase-crashlytics-gradle:${Versions.Firebase.crashlytics}"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    }
}