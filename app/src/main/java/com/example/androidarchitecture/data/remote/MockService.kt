package com.example.androidarchitecture.data.remote

import android.content.SharedPreferences
import com.example.androidarchitecture.data.local.UserPreference
import com.example.androidarchitecture.data.model.*
import com.example.androidarchitecture.data.model.request.SignInRequest
import com.example.androidarchitecture.data.model.response.SignInResponse
import io.reactivex.Single
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockService @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private val TAG = UserPreference::class.java.simpleName
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

    private val accessToken = "JJAS92UEIDKAXSG8W"
    private var user: User? = getUser()

    fun signIn(signInRequest: SignInRequest): Single<ApiResponse<SignInResponse?>?> {
        user?.let { user ->
            if (user.email == signInRequest.email) {
                return Single.create { emitter ->
                    emitter.onSuccess(ApiResponse(Status.SIGN_IN, SignInResponse(accessToken, user)))
                }
            } else {
                return Single.create { emitter ->
                    emitter.onSuccess(ApiResponse(Status.INVALID_CREDENTIALS, null, "Please enter valid credentials"))
                }
            }
        } ?: run {
            return Single.create { emitter ->
                user = User(7, signInRequest.email)
                emitter.onSuccess(ApiResponse(Status.SIGN_UP, SignInResponse(accessToken, user!!)))
            }
        }
    }

    fun getProfile(accessToken: String?, id: Long): Single<ApiResponse<User?>?> {
        if (accessToken != this.accessToken) {
            return Single.create { emitter ->
                emitter.onSuccess(ApiResponse(Status.INVALID_ACCESS_TOKEN, null))
            }
        }

        user?.takeIf { id == it.id }?.let { user ->
            return Single.create { emitter ->
                emitter.onSuccess(ApiResponse(Status.SUCCESS, user))
            }
        } ?: run {
            return Single.create { emitter ->
                emitter.onSuccess(ApiResponse(Status.INVALID_USER, null))
            }
        }
    }

    fun updateProfile(accessToken: String?, input: User): Single<ApiResponse<User?>?> {
        if (accessToken != this.accessToken) {
            return Single.create { emitter ->
                emitter.onSuccess(ApiResponse(Status.INVALID_ACCESS_TOKEN, null))
            }
        }

        user?.let {
            user = input
            return Single.create { emitter ->
                emitter.onSuccess(ApiResponse(Status.SUCCESS, user))
            }
        } ?: run {
            return Single.create { emitter ->
                emitter.onSuccess(ApiResponse(Status.INVALID_USER, null))
            }
        }
    }

    fun uploadImage(image: File?): Single<Pair<Status, String?>> {
        image?.let {
            return Single.create { emitter ->
                emitter.onSuccess(Pair(Status.SUCCESS, image.path))
            }
        } ?: run {
            return Single.create { emitter ->
                emitter.onSuccess(Pair(Status.IMAGE_UPLOAD_FAILED, null))
            }
        }
    }

    private fun getUser(): User? {
        val id: Long = sharedPreferences.getLong(KEY_ID, 0L)
        val email: String? = sharedPreferences.getString(KEY_EMAIL, null)
        val firstName: String? = sharedPreferences.getString(KEY_FIRST_NAME, null)
        val lastName: String? = sharedPreferences.getString(KEY_LAST_NAME, null)
        val bio: String? = sharedPreferences.getString(KEY_BIO, null)
        val avatar: String? = sharedPreferences.getString(KEY_AVATAR, null)
        val cityId: Int = sharedPreferences.getInt(KEY_CITY_ID, 0)
        val cityName: String? = sharedPreferences.getString(KEY_CITY_NAME, null)
        val gender: String? = sharedPreferences.getString(KEY_GENDER, null)

        var user: User? = null
        var city: City? = null
        var genderVal: Gender? = null
        if (cityId != 0 && !cityName.isNullOrBlank()) {
            city = City(cityId, cityName!!)
        }
        if (!gender.isNullOrBlank()) {
            genderVal = Gender.valueOf(gender!!)
        }
        if (id != 0L && !email.isNullOrBlank()) {
            user = User(id, email!!, firstName, lastName, bio, avatar, city, genderVal)
        }
        return user
    }
}