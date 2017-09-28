package com.example.androidarchitecture.di

import com.example.androidarchitecture.ui.editprofile.EditProfileFragment
import com.example.androidarchitecture.ui.home.HomeFragment
import com.example.androidarchitecture.ui.signin.SignInFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSignInFragment(): SignInFragment

    @ContributesAndroidInjector
    internal abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    internal abstract fun contributeEditProfileFragment(): EditProfileFragment
}