package com.example.androidarchitecture.di

import com.example.androidarchitecture.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = arrayOf(FragmentBuilderModule::class))
    internal abstract fun contributeSplashActivity(): SplashActivity
}