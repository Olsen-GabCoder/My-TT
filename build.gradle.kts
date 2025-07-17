// Fichier : build.gradle.kts (Project: MyTT)
// Ce fichier de configuration de haut niveau s'applique à l'ensemble du projet.

plugins {
    // Plugin de base pour les applications Android.
    alias(libs.plugins.android.application) apply false

    // Plugin de base pour le langage Kotlin.
    alias(libs.plugins.kotlin.android) apply false

    // Plugin pour les services Google (nécessaire pour Firebase).
    alias(libs.plugins.google.gms.services) apply false

    // NOUVEAU : Déclare le plugin Hilt au niveau du projet pour l'injection de dépendances.
    // Il doit être disponible avant que le module 'app' ne puisse l'utiliser.
    alias(libs.plugins.hilt.gradle) apply false

    // NOUVEAU : Déclare le plugin Safe Args pour la navigation sécurisée entre les écrans.
    alias(libs.plugins.navigation.safeargs) apply false
}