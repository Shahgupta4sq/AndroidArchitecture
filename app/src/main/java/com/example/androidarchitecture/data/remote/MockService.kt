package com.example.androidarchitecture.data.remote

import android.net.Uri
import com.example.androidarchitecture.data.local.UserPreference
import com.example.androidarchitecture.data.model.ApiResponse
import com.example.androidarchitecture.data.model.Status
import com.example.androidarchitecture.data.model.User
import com.example.androidarchitecture.data.model.request.SignInRequest
import com.example.androidarchitecture.data.model.response.SignInResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockService @Inject constructor(userPreference: UserPreference) {

    private val accessToken = "JJAS92UEIDKAXSG8W"
    private var user: User? = userPreference.getMockUser()

    fun signIn(signInRequest: SignInRequest): Single<ApiResponse<SignInResponse?>?> {
        user?.let { user ->
            if (user.email == signInRequest.email) {
                return Single.create { emitter ->
                    Thread.sleep(100)
                    emitter.onSuccess(ApiResponse(Status.SIGN_IN, SignInResponse(accessToken, this.user!!)))
                }
            } else {
                return Single.create { emitter ->
                    Thread.sleep(100)
                    emitter.onSuccess(ApiResponse(Status.INVALID_CREDENTIALS, null, "Please enter valid credentials"))
                }
            }
        } ?: run {
            return Single.create { emitter ->
                Thread.sleep(100)
                user = User(7, signInRequest.email!!)
                emitter.onSuccess(ApiResponse(Status.SIGN_UP, SignInResponse(accessToken, user!!)))
            }
        }
    }

    fun getProfile(accessToken: String?, id: Long): Single<ApiResponse<User?>?> {
        if (accessToken != this.accessToken) {
            return Single.create { emitter ->
                Thread.sleep(100)
                emitter.onSuccess(ApiResponse(Status.INVALID_ACCESS_TOKEN, null))
            }
        }

        user?.takeIf { id == it.id }?.let { user ->
            return Single.create { emitter ->
                Thread.sleep(100)
                emitter.onSuccess(ApiResponse(Status.SUCCESS, user))
            }
        } ?: run {
            return Single.create { emitter ->
                Thread.sleep(100)
                emitter.onSuccess(ApiResponse(Status.INVALID_USER, null))
            }
        }
    }

    fun updateProfile(accessToken: String?, input: User): Single<ApiResponse<User?>?> {
        if (accessToken != this.accessToken) {
            return Single.create { emitter ->
                Thread.sleep(100)
                emitter.onSuccess(ApiResponse(Status.INVALID_ACCESS_TOKEN, null))
            }
        }

        user?.let {
            user = input
            return Single.create { emitter ->
                Thread.sleep(100)
                emitter.onSuccess(ApiResponse(Status.SUCCESS, user))
            }
        } ?: run {
            return Single.create { emitter ->
                Thread.sleep(100)
                emitter.onSuccess(ApiResponse(Status.INVALID_USER, null))
            }
        }
    }

    fun uploadImage(image: Uri?): Single<Pair<Status, String?>> {
        image?.let {
            return Single.create { emitter ->
                Thread.sleep(100)
                emitter.onSuccess(Pair(Status.SUCCESS, image.toString()))
            }
        } ?: run {
            return Single.create { emitter ->
                Thread.sleep(100)
                emitter.onSuccess(Pair(Status.IMAGE_UPLOAD_FAILED, null))
            }
        }
    }
}