package com.example.androidarchitecture.ui.signin

import android.arch.lifecycle.ViewModel
import com.example.androidarchitecture.data.UserRepository
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){

}