package com.mytt.app.core.data.repository

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import com.mytt.app.core.data.models.User

interface AuthRepository {

    suspend fun login(email: String, password: String): Result<User>

    fun startPhoneNumberVerification(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    )

    suspend fun signInWithPhoneAuthCredential(verificationId: String, otpCode: String): Result<User>

    suspend fun registerUser(name: String, phoneNumber: String, email: String, password: String): Result<User>

    suspend fun checkIfPhoneNumberExists(phoneNumber: String): Result<Boolean>

    suspend fun getCurrentUser(): Result<User>

    // MODIFICATION : Ajout de la fonction de déconnexion au contrat.
    // C'est une fonction simple qui n'a pas besoin de retourner de valeur.
    fun logout()
}