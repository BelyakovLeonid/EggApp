buildscript {

    repositories {
        google()
        jcenter()
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
        jcenter()
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}