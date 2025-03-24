plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Chỉ giữ lại một lần
}

android {
    namespace = "com.pixelcard.project_truyen_as"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pixelcard.project_truyen_as"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Firebase BOM quản lý version các thư viện Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))

    // Firebase Services
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-analytics")

    // Facebook Login SDK
    implementation("com.facebook.android:facebook-login:16.3.0") // Thay thế `libs.facebook.login`

    // Google Play Services for Authentication
    implementation("com.google.android.gms:play-services-auth:20.7.0") // Thay thế `libs.credentials.play.services.auth`

    // UI & AndroidX Components
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.activity:activity-ktx:1.8.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation(libs.activity) // Thay thế `libs.viewpager2`

    // Testing Libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
