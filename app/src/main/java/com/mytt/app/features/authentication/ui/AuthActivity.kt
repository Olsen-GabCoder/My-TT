package com.mytt.app.features.authentication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mytt.app.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activité qui gère le flux d'authentification de l'utilisateur.
 * Elle sert de point d'entrée et de conteneur pour les fragments
 * de connexion, d'inscription, de réinitialisation de mot de passe, etc.
 *
 * L'annotation @AndroidEntryPoint permet à Hilt d'y injecter des dépendances.
 */
@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pour l'instant, l'activité se contente d'afficher son layout.
        // La configuration du graphe de navigation qui gérera les fragments
        // sera effectuée dans une étape ultérieure.
    }
}