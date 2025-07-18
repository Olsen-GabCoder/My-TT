// Fichier : app/src/main/java/com/mytt/app/features/client/MainActivity.kt

package com.mytt.app.features.client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mytt.app.R
import com.mytt.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activité principale pour l'utilisateur final (client).
 * C'est le point d'entrée après une connexion réussie.
 * MODIFIÉ : Gère maintenant la BottomNavigationView.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- DEBUT DE LA MODIFICATION ---

        // 1. Récupérer le NavController depuis notre NavHostFragment.
        // Le NavHostFragment est le conteneur (défini dans activity_main.xml) qui gère l'affichage des fragments.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_client) as NavHostFragment
        val navController = navHostFragment.navController

        // 2. Connecter la BottomNavigationView avec le NavController.
        // C'est cette ligne qui lie les clics sur les onglets de la barre de navigation
        // aux destinations correspondantes dans notre graphe de navigation (client_nav_graph.xml).
        // Elle se base sur la correspondance des ID (ex: id de l'item de menu == id du fragment dans le nav_graph).
        binding.bottomNavView.setupWithNavController(navController)

        // --- FIN DE LA MODIFICATION ---
    }
}