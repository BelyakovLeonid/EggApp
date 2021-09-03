buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.TopLevel.gradle)
        classpath(Dependencies.TopLevel.gradlePlugin)
        classpath(Dependencies.TopLevel.safeArgs)
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