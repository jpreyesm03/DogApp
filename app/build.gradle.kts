plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "be.kuleuven.gt.dogapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "be.kuleuven.gt.dogapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    //noinspection UseTomlInstead
    implementation("com.android.volley:volley:1.2.1")

    //noinspection UseTomlInstead
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    //noinspection UseTomlInstead
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:custom-ui:12.1.0")

    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //noinspection UseTomlInstead

    // You can check for the latest version on Google's Maven Repository:
    // implementation 'com.google.android.youtube:youtube-android-player:latest_version'
}