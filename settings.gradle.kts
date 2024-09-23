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
        maven { setUrl("https://jitpack.io") }
        maven {
            setUrl("https://repo.spring.io/release")
        }
        maven {
            setUrl("https://repository.jboss.org/maven2")
        }
        gradlePluginPortal()
        jcenter()
        maven {
            setUrl("https://maven.google.com")
        }
        maven {
            setUrl("https://artifact.bytedance.com/repository/pangle/")
        }
        maven {
            setUrl("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven {
            setUrl("https://repo.spring.io/release")
        }

        maven {
            setUrl("https://repository.jboss.org/maven2")
        }
        jcenter()
        maven {
            setUrl("https://maven.google.com")
        }
        maven {
            setUrl("https://artifact.bytedance.com/repository/pangle/")
        }
        maven {
            setUrl("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        }
    }
}

include(":app")
rootProject.name = "FluidPion"
include(":commonRes")
include(":LibAds")
include(":LibIAP")
