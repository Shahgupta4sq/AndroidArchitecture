package com.example.androidarchitecture.di

import com.example.androidarchitecture.base.BaseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSignInFragment(): BaseFragment
}