package com.mytt.app.core.data.models

/**
 * Classe de données représentant un utilisateur dans la base de données Firestore.
 * Elle contient des informations complémentaires à celles de Firebase Auth.
 *
 * @param uid L'identifiant unique de l'utilisateur (doit correspondre à l'UID de Firebase Auth).
 * @param email L'adresse e-mail de l'utilisateur.
 * @param role Le rôle de l'utilisateur ("client" ou "admin").
 * @param displayName Le nom d'affichage de l'utilisateur.
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val role: String = "client",
    val displayName: String = ""
)