package com.example.androidarchitecture.di

import com.example.androidarchitecture.ui.signin.SignInFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSignInFragment(): SignInFragment
}