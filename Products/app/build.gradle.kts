plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"

    // Hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")

    // KSP (for Room)
    id("com.google.devtools.ksp")
}

kotlin {
    jvmToolchain(21)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "com.cornmuffin.products"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cornmuffin.products"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Set by default by Android Studio
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2024.09.03"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.03"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Added later
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testCompileOnly("pl.pragmatists:JUnitParams:1.1.1")
    implementation("io.coil-kt:coil:2.5.0")
    implementation("io.coil-kt:coil-compose:2.5.0")

    val navVersion = "2.8.2"
    implementation("androidx.navigation:navigation-compose:$navVersion")
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    // https://dev.to/andreym/how-to-do-a-material-3-pull-refresh-15b0
    implementation("eu.bambooapps:compose-material3-pullrefresh:1.0.0")

    val hiltVersion = "2.48.1" // 1/18/2024 Be careful about updating this.  2.50 doesn't seem to compile with kapt or ksp or something.
    val hiltNavigationComposeVersion = "1.2.0"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion")
    implementation("androidx.hilt:hilt-navigation-fragment:$hiltNavigationComposeVersion")

    // Room https://developer.android.com/training/data-storage/room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    testImplementation("androidx.room:room-testing:$roomVersion")

    // OrbitMVI
    val orbitVersion = "6.1.0"
    implementation("org.orbit-mvi:orbit-core:$orbitVersion")

    // Mockk
    val mockkVersion = "1.13.9"
    testImplementation("io.mockk:mockk:$mockkVersion")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
