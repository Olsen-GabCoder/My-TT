package com.mytt.app.core.data.models

import com.google.firebase.Timestamp

/**
 * Classe de données représentant un utilisateur dans la base de données Firestore.
 * VERSION 2 : Enrichie pour l'authentification par téléphone et l'enregistrement.
 *
 * @param uid L'identifiant unique de l'utilisateur (doit correspondre à l'UID de Firebase Auth).
 * @param phoneNumber Le numéro de téléphone, qui est le principal identifiant de connexion.
 * @param name Le nom complet de l'utilisateur (saisi lors de l'enregistrement).
 * @param email L'adresse e-mail, maintenant optionnelle, pour la récupération ou les communications.
 * @param role Le rôle de l'utilisateur ("client" ou "admin").
 * @param photoUrl L'URL de l'image de profil de l'utilisateur (optionnel).
 * @param createdAt La date et l'heure de création du compte. Utile pour l'analyse.
 * @param balance Le solde actuel du compte de l'utilisateur.
 */
data class User(
    val uid: String = "",
    val phoneNumber: String = "",
    val name: String = "",
    val email: String? = null, // Rendu optionnel (nullable)
    val role: String = "client",
    val photoUrl: String? = null, // Optionnel
    val createdAt: Timestamp? = null, // Géré par Firebase Server Timestamp
    // MODIFICATION : Ajout du champ 'balance' avec une valeur par défaut pour la robustesse.
    val balance: Double = 0.0
)