package com.mytt.app.core.data.repository

import android.app.Activity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mytt.app.core.data.models.User
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// MODIFICATION : Le nom du fichier est incorrect dans votre code. Il devrait être AuthRepositoryImpl.kt.
// Je vais supposer que c'est une coquille et que le nom de la classe est bien AuthRepositoryImpl.
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    // ... (toutes les autres fonctions : login, startPhoneNumberVerification, etc. restent inchangées) ...

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: throw IllegalStateException("Firebase User ne peut pas être nul après une connexion réussie.")
            val userDocument = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()
            val user = userDocument.toObject(User::class.java)
                ?: throw IllegalStateException("Document utilisateur non trouvé ou invalide dans Firestore.")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun startPhoneNumberVerification(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun signInWithPhoneAuthCredential(verificationId: String, otpCode: String): Result<User> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user
                ?: throw IllegalStateException("Firebase User ne peut pas être nul après une vérification réussie.")

            val phoneNumber = firebaseUser.phoneNumber
                ?: throw IllegalStateException("Le numéro de téléphone ne peut pas être nul après une connexion OTP.")

            val nationalPhoneNumber = phoneNumber.takeLast(8)
            val queryResult = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("phoneNumber", nationalPhoneNumber)
                .limit(1)
                .get()
                .await()

            if (queryResult.isEmpty) {
                throw IllegalStateException("Document utilisateur non trouvé ou invalide dans Firestore.")
            }

            val userDocument = queryResult.documents.first()
            val user = userDocument.toObject(User::class.java)
                ?: throw IllegalStateException("Le document utilisateur est invalide ou mal formaté.")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerUser(name: String, phoneNumber: String, email: String, password: String): Result<User> {
        try {
            val phoneCheckResult = checkIfPhoneNumberExists(phoneNumber)
            phoneCheckResult.onFailure { return Result.failure(it) }
            val phoneExists = phoneCheckResult.getOrThrow()
            if (phoneExists) {
                return Result.failure(Exception("phone number is already in use"))
            }

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: throw IllegalStateException("La création de l'utilisateur a échoué dans Firebase Auth.")

            val newUser = User(
                uid = firebaseUser.uid,
                phoneNumber = phoneNumber,
                name = name,
                email = email,
                role = "client",
                createdAt = Timestamp.now()
            )
            firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(newUser)
                .await()

            return Result.success(newUser)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun checkIfPhoneNumberExists(phoneNumber: String): Result<Boolean> {
        return try {
            val queryResult = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("phoneNumber", phoneNumber)
                .limit(1)
                .get()
                .await()
            Result.success(!queryResult.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val firebaseUser = auth.currentUser
                ?: throw IllegalStateException("Aucun utilisateur n'est actuellement connecté.")
            val userDocument = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()
            val user = userDocument.toObject(User::class.java)
                ?: throw IllegalStateException("Le document utilisateur de la session actuelle est manquant ou invalide.")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * MODIFICATION : Implémentation de la fonction logout.
     * La logique est une simple délégation à Firebase Auth.
     */
    override fun logout() {
        auth.signOut()
    }
}