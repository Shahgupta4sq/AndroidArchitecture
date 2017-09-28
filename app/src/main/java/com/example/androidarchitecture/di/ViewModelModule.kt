package com.example.androidarchitecture.di

import android.arch.lifecycle.ViewModel
import com.example.androidarchitecture.ui.editprofile.EditProfileViewModel
import com.example.androidarchitecture.ui.signin.SignInViewModel
import com.example.androidarchitecture.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun bindSignInViewModel(signInViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    internal abstract fun bindEditProfileViewModel(editProfileViewModel: EditProfileViewModel): ViewModel
}