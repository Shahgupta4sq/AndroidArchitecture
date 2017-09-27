package com.example.androidarchitecture.data.local

import android.content.SharedPreferences
import com.example.androidarchitecture.data.model.City
import com.example.androidarchitecture.data.model.Gender
import com.example.androidarchitecture.data.model.User
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreference @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private val TAG = this::class.java.simpleName
        private val KEY_ACCESS_TOKEN = "$TAG.KEY_ACCESS_TOKEN"
        private val KEY_ID = "$TAG.KEY_ID"
        private val KEY_EMAIL = "$TAG.KEY_EMAIL"
        private val KEY_FIRST_NAME = "$TAG.KEY_FIRST_NAME"
        private val KEY_LAST_NAME = "$TAG.KEY_LAST_NAME"
        private val KEY_BIO = "$TAG.KEY_BIO"
        private val KEY_AVATAR = "$TAG.KEY_AVATAR"
        private val KEY_CITY_ID = "$TAG.KEY_CITY_ID"
        private val KEY_CITY_NAME = "$TAG.KEY_CITY_NAME"
        private val KEY_GENDER = "$TAG.KEY_GENDER"
    }

    private var accessToken: String? = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    private var id: Long = sharedPreferences.getLong(KEY_ID, 0L)
    private var email: String? = sharedPreferences.getString(KEY_EMAIL, null)
    private var firstName: String? = sharedPreferences.getString(KEY_FIRST_NAME, null)
    private var lastName: String? = sharedPreferences.getString(KEY_LAST_NAME, null)
    private var bio: String? = sharedPreferences.getString(KEY_BIO, null)
    private var avatar: String? = sharedPreferences.getString(KEY_AVATAR, null)
    private var cityId: Int = sharedPreferences.getInt(KEY_CITY_ID, 0)
    private var cityName: String? = sharedPreferences.getString(KEY_CITY_NAME, null)
    private var gender: String? = sharedPreferences.getString(KEY_GENDER, null)

    private val userPublisher: BehaviorSubject<User?> = BehaviorSubject.create()

    fun setAccessToken(accessToken: String) {
        this.accessToken = accessToken
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun getAccessToken() = accessToken

    fun getId() = id

    fun setUser(user: User) {
        id = user.id
        email = user.email
        firstName = user.firstName
        lastName = user.lastName
        bio = user.bio
        avatar = user.avatar
        user.city?.let {
            cityId = it.id
            cityName = it.name
        }
        user.gender?.let { gender = it.toString() }

        userPublisher.onNext(user)

        val editor = sharedPreferences.edit()
        editor.putLong(KEY_ID, id)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_FIRST_NAME, firstName)
        editor.putString(KEY_LAST_NAME, lastName)
        editor.putString(KEY_BIO, bio)
        editor.putString(KEY_AVATAR, avatar)
        editor.putInt(KEY_CITY_ID, cityId)
        editor.putString(KEY_CITY_NAME, cityName)
        editor.putString(KEY_GENDER, gender)
        editor.apply()
    }

    fun getUser(): Flowable<User?> {
        var user: User? = null
        var city: City? = null
        var gender: Gender? = null
        if (cityId != 0 && !cityName.isNullOrBlank()) {
            city = City(cityId, cityName!!)
        }
        if (!this.gender.isNullOrBlank()) {
            gender = Gender.valueOf(this.gender!!)
        }
        if (id != 0L && !email.isNullOrBlank()) {
            user = User(id, email!!, firstName, lastName, bio, avatar, city, gender)
        }
        if (userPublisher.value == null && user != null) {
            userPublisher.onNext(user)
        }
        return userPublisher.toFlowable(BackpressureStrategy.BUFFER)
    }

    fun isSignedIn(): Boolean {
        return !accessToken.isNullOrEmpty()
    }

    fun signOut() {
        accessToken = null
        id = 0L
        email = null
        firstName = null
        lastName = null
        bio = null
        avatar = null
        cityId = 0
        cityName = null
        gender = null
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}