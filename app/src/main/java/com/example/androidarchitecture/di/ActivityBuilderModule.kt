package com.example.androidarchitecture.di

import com.example.androidarchitecture.ui.base.FragmentContainerActivity
import com.example.androidarchitecture.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = arrayOf(FragmentBuilderModule::class))
    internal abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = arrayOf(FragmentBuilderModule::class))
    internal abstract fun contributeFragmentContainerActivity(): FragmentContainerActivity
}