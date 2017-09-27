package com.example.androidarchitecture.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.androidarchitecture.data.remote.MockService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Singleton
    @Provides
    fun provideMockService(): MockService {
        return MockService()
    }
}