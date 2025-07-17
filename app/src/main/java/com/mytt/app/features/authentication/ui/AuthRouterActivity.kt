package com.mytt.app.features.authentication.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mytt.app.databinding.ActivityAuthRouterBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activité d'aiguillage lancée au démarrage.
 * Son unique rôle est de décider vers quelle activité principale naviguer.
 */
@AndroidEntryPoint
class AuthRouterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthRouterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthRouterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // La logique d'aiguillage sera ici.
        // Pour le moment, nous considérons que l'utilisateur n'est jamais connecté.
        navigateToAuthActivity()
    }

    /**
     * NOUVEAU : Fonction qui gère la navigation vers l'écran d'authentification.
     */
    private fun navigateToAuthActivity() {
        // 1. On crée une "intention" de démarrer AuthActivity.
        //    L'Intent est le message standard d'Android pour demander une action,
        //    ici, "démarrer un autre composant de l'application".
        val intent = Intent(this, AuthActivity::class.java)

        // 2. On exécute l'intention.
        startActivity(intent)

        // 3. On termine l'activité actuelle (AuthRouterActivity).
        //    Ceci est crucial pour que l'utilisateur ne puisse pas revenir
        //    à cet écran d'aiguillage en appuyant sur le bouton "Retour".
        //    Une fois sa mission accomplie, elle doit disparaître de la pile de navigation.
        finish()
    }
}