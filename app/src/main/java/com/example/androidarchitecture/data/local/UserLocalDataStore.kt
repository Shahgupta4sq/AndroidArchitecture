package com.example.androidarchitecture.data.local

import com.example.androidarchitecture.data.model.User
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataStore @Inject constructor(private val userPreference: UserPreference) {

    fun signIn(accessToken: String, user: User) {
        userPreference.setAccessToken(accessToken)
        userPreference.setUser(user)
    }

    fun isSignedIn(): Boolean {
        return userPreference.isSignedIn()
    }

    fun getAccessToken() = userPreference.getAccessToken()

    fun getId() = userPreference.getId()

    fun getProfile(): Flowable<User?> {
        return userPreference.getUser()
    }

    fun saveProfile(user: User) {
        userPreference.setUser(user)
    }

    fun signOut() {
        userPreference.signOut()
    }
}