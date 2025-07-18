package com.mytt.app.features.authentication.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mytt.app.core.data.models.User
import com.mytt.app.core.data.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implémentation concrète de l'AuthRepository utilisant Firebase.
 */
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    // NOUVEAU : Injection de Firestore.
    private val firestore: FirebaseFirestore
) : AuthRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Étape 1 : Authentifier l'utilisateur avec Firebase Auth.
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: throw IllegalStateException("Firebase User ne peut pas être nul après une connexion réussie.")

            // Étape 2 : Utiliser l'UID de l'utilisateur authentifié pour récupérer son document dans Firestore.
            val userDocument = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val user = userDocument.toObject(User::class.java)
                ?: throw IllegalStateException("Document utilisateur non trouvé ou invalide dans Firestore.")

            // Étape 3 : Si tout a réussi, retourner l'objet User complet.
            Result.success(user)
        } catch (e: Exception) {
            // Si une exception se produit à n'importe quelle étape, la capturer.
            Result.failure(e)
        }
    }
}