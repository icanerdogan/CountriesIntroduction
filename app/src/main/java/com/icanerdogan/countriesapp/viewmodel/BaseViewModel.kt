package com.icanerdogan.countriesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

// Feed View Model içinde hem ViewModel hem de Coroutines kullanılması gerektiğinden dolayı Base olan bir sınıf oluşturduk!
abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    // Arka planda çalışacak iş nesnesi oluşturuyoruz!
    private val job = Job()

    override val coroutineContext: CoroutineContext
    // Arka planda bu işi yap ve Main Threade dön
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}