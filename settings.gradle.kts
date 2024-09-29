pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://maven.google.com/")
    }
}

rootProject.name = "FileLocker"
include(":presentation")
include(":domain")
include(":data")
include(":admob")
include(":photoview")
include(":patternlockview")
