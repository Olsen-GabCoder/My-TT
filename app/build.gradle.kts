// Fichier : app/build.gradle.kts (Module: app)
// Ce fichier configure spécifiquement le module principal de l'application.

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.services)

    // NOUVEAU : Active le plugin Hilt pour ce module.
    alias(libs.plugins.hilt.gradle)

    // NOUVEAU : Active le plugin Safe Args pour ce module.
    alias(libs.plugins.navigation.safeargs)

    // NOUVEAU : Plugin essentiel pour le traitement des annotations en Kotlin.
    // Hilt en a besoin pour générer du code.
    kotlin("kapt")
}

android {
    namespace = "com.mytt.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mytt.app"
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
    }
}

dependencies {

    // --- Cœur AndroidX & UI ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)
    // NOUVEAU : Ajout de la dépendance pour les fragments, essentielle pour la navigation.
    implementation(libs.androidx.fragment.ktx)

    // --- Architecture (MVVM, Navigation, Injection de Dépendances) ---
    // NOUVEAU : Dépendances pour ViewModel et LiveData.
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Dépendances pour Navigation Component.
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // NOUVEAU : Dépendances pour Hilt.
    implementation(libs.hilt.android)
    // 'kapt' est utilisé ici pour le processeur d'annotations de Hilt.
    kapt(libs.hilt.compiler)

    // --- Firebase & Google Play Services ---
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.functions)
    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.play.services.auth)
    implementation("com.google.firebase:firebase-appcheck-ktx")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    debugImplementation("com.google.firebase:firebase-appcheck-debug")
    implementation("io.coil-kt:coil:2.4.0")

    // --- Tests ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// NOUVEAU : Bloc de configuration pour Kapt, requis par Hilt.
kapt {
    correctErrorTypes = true
}