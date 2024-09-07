plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.android.testdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.testdemo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }


    signingConfigs {
        create("release") {
            storeFile = file("D:\\Android12SignerGUI\\SignFiles\\OC195\\platform.jks")
            keyAlias = "android"
            keyPassword = "android"
            storePassword = "android"
        }
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}