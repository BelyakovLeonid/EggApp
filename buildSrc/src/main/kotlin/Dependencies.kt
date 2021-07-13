object Dependencies {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val viewBindingDelegate = "com.kirich1409.viewbindingpropertydelegate:vbpd-noreflection:${Versions.viewBindingDelegate}"

    object TopLevel{
        val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
        val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    }

    object Tests {
        val junit = "junit:junit:${Versions.junit}"
        val androidJunit = "androidx.test.ext:junit:${Versions.androidJunit}"
        val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    object Jetpack {
        val core = "androidx.core:core-ktx:${Versions.jetpackCore}"
        val fragment = "androidx.fragment:fragment-ktx:${Versions.jetpackFragment}"
    }

    object Ui {
        val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
        val material = "com.google.android.material:material:${Versions.material}"
        val konfetti = "nl.dionsegijn:konfetti:${Versions.konfetti}"
    }

    object Navigation {
        val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    }

    object Dagger {
        val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }
}