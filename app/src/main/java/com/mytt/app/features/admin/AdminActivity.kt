package com.mytt.app.features.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mytt.app.databinding.ActivityAdminBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activité principale pour l'administrateur (agent Tunisie Telecom).
 * C'est le point d'entrée après une connexion réussie d'un admin.
 */
@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}