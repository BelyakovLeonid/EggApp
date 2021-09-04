buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.TopLevel.gradle)
        classpath(Dependencies.TopLevel.gradlePlugin)
        classpath(Dependencies.TopLevel.safeArgs)
        classpath(Dependencies.Firebase.core)
        classpath(Dependencies.Firebase.crashlyticsGradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}