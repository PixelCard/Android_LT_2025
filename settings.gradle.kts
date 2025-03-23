@file:Suppress("DEPRECATION")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        google()
        gradlePluginPortal()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        maven(url = "https://jitpack.io")

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }
}

rootProject.name = "project_Truyen_AS"
include(":app")
 