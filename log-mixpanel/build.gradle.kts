import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")

    id("com.vanniktech.maven.publish") version "0.32.0"
}

android {
    namespace = "android.boot.slf.mixpanel"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.slf.api)
//    implementation(project(":api"))
    kapt(libs.auto.service)
    implementation(libs.auto.service.annotations)
    implementation(libs.mixpanel.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    configure(
        AndroidSingleVariantLibrary(
            // the published variant
            variant = "release",
            // whether to publish a sources jar
            sourcesJar = true,
            // whether to publish a javadoc jar
            publishJavadocJar = true,
        )
    )

    coordinates("io.github.zhangwenxue", "slf-mixpanel", "1.0.1")

    pom {
        name.set("Slf-Mixpanel")
        description.set("An Android simple log facade api")
        inceptionYear.set("2024")
        url.set("https://github.com/zhangwenxue/slf/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("zwx")
                name.set("zhangwenxue")
                url.set("https://github.com/zhangwenxue/")
            }
        }
        scm {
            url.set("https://github.com/zhangwenxue/slf/")
            connection.set("scm:git:git://github.com/zhangwenxue/slf.git")
            developerConnection.set("scm:git:ssh://git@github.com/zhangwenxue/slf.git")
        }
    }
    signAllPublications()
}
