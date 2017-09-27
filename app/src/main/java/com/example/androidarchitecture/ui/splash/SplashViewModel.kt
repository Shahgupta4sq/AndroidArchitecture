package com.example.androidarchitecture.ui.splash

import android.arch.lifecycle.ViewModel
import com.example.androidarchitecture.data.UserRepository
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun isSignedIn(): Boolean {
        return userRepository.isSignedIn()
    }
}