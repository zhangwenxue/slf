pluginManagement {
    repositories {
//        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
//        maven(url = "https://maven.aliyun.com/repository/public")
//        maven(url = "https://maven.aliyun.com/repository/central")
//        maven(url = "https://maven.aliyun.com/repository/google")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/central")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://jitpack.io")
        google()
    }
}

rootProject.name = "slf"
include(":app")
include(":api")
include(":log-shipbook")
include(":log-mixpanel")
