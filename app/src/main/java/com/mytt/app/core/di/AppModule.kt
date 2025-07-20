package com.mytt.app.core.di

import com.mytt.app.core.data.repository.AuthRepository
import com.mytt.app.core.data.repository.OffersRepository
import com.mytt.app.core.data.repository.AuthRepositoryImpl
import com.mytt.app.features.client.offers.data.repository.OffersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module Hilt qui gère les bindings (liaisons) des interfaces de Repository
 * pour toute l'application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Lie l'interface AuthRepository à son implémentation.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    /**
     * Lie l'interface OffersRepository à son implémentation.
     */
    @Binds
    @Singleton
    abstract fun bindOffersRepository(impl: OffersRepositoryImpl): OffersRepository

}