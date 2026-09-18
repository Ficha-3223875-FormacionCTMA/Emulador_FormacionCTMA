plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.miformacionctma"

    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.miformacionctma"

        minSdk = 24
        targetSdk = 34

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}
kotlin {
    jvmToolchain(17)
}


/*
 * Configuración de KSP para Room
 */
ksp {
    arg(
        "room.schemaLocation",
        "$projectDir/schemas"
    )
}


dependencies {

    // -----------------------------------------
    // Jetpack Compose
    // -----------------------------------------

    implementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    implementation(
        libs.androidx.activity.compose
    )

    implementation(
        libs.androidx.material3
    )

    implementation(
        libs.androidx.ui
    )

    implementation(
        libs.androidx.ui.graphics
    )

    implementation(
        libs.androidx.ui.tooling.preview
    )

    implementation(
        libs.androidx.core.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7"
    )

    implementation(
        "androidx.lifecycle:lifecycle-runtime-compose:2.8.7"
    )


    // -----------------------------------------
    // Room Database
    // -----------------------------------------

    val roomVersion = "2.6.1"

    implementation(
        "androidx.room:room-runtime:$roomVersion"
    )

    implementation(
        "androidx.room:room-ktx:$roomVersion"
    )

    ksp(
        "androidx.room:room-compiler:$roomVersion"
    )


    // -----------------------------------------
    // DataStore
    // -----------------------------------------

    implementation(
        "androidx.datastore:datastore-preferences:1.1.2"
    )


    // -----------------------------------------
    // Retrofit
    // -----------------------------------------

    implementation(
        "com.squareup.retrofit2:retrofit:2.11.0"
    )

    implementation(
        "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0"
    )

    implementation(
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3"
    )

    implementation(
        "com.squareup.okhttp3:okhttp:4.12.0"
    )


    // -----------------------------------------
    // Pruebas unitarias
    // -----------------------------------------

    testImplementation(
        "junit:junit:4.13.2"
    )

    testImplementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0"
    )

    testImplementation(
        "org.mockito:mockito-core:5.14.2"
    )

    testImplementation(
        "org.mockito.kotlin:mockito-kotlin:5.4.0"
    )

    testImplementation(
        "com.squareup.okhttp3:mockwebserver:4.12.0"
    )


    // -----------------------------------------
    // Pruebas UI e instrumentadas
    // -----------------------------------------

    androidTestImplementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    androidTestImplementation(
        libs.androidx.ui.test.junit4
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.junit
    )

    debugImplementation(
        libs.androidx.ui.test.manifest
    )

    debugImplementation(
        libs.androidx.ui.tooling
    )


    // -----------------------------------------
    // Coil
    // -----------------------------------------

    implementation(
        "io.coil-kt:coil-compose:2.6.0"
    )


    // -----------------------------------------
    // Gson
    // -----------------------------------------

    implementation(
        "com.squareup.retrofit2:converter-gson:2.9.0"
    )
}