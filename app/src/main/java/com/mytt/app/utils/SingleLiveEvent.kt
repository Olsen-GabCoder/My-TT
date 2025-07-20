package com.mytt.app.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Un MutableLiveData qui ne notifie les observateurs qu'une seule fois pour chaque mise à jour.
 * Utile pour les événements comme la navigation, les Toasts, ou les Snackbars.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    /**
     * Utilisé pour les appels sans valeur (par exemple, pour les événements de type Unit).
     */
    @MainThread
    fun call() {
        value = null
    }
}