plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    //Args for navigation
    id("androidx.navigation.safeargs.kotlin")
    //firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.talkey_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.talkey_android"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {

    //firebase
    implementation("com.google.firebase:firebase-messaging")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //biometric
    implementation("androidx.biometric:biometric:1.2.0-alpha05")

    //splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-simplexml:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:compiler:4.16.0")
    ksp("com.github.bumptech.glide:ksp:4.16.0")
    implementation("com.caverock:androidsvg:1.4")

    //Lifecycle & Coroutines
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    //Navigation
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    // Activity
    implementation("androidx.activity:activity-ktx:1.8.2")
    //Swiperefreshlayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Compose
//    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
//    implementation ("androidx.compose.material:material:1.6.4") //1.5.4
//    implementation ("androidx.compose.ui:ui:1.6.4") //1.5.4
//    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}