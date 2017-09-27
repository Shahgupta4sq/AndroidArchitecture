package com.example.androidarchitecture.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
        ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(viewModelClass: Class<T>?): T {
        var creator: Provider<out ViewModel>? = null
        viewModelClass?.let { modelClass ->
            creator = creators[modelClass]
            if (creator == null) {
                for ((key, value) in creators) {
                    if (modelClass.isAssignableFrom(key)) {
                        creator = value
                        break
                    }
                }
            }
        }

        creator?.let {
            try {
                return it.get() as T
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        } ?: run { throw IllegalArgumentException("unknown model class " + viewModelClass) }
    }
}