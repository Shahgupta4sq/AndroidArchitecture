package com.example.androidarchitecture.data.remote

import com.example.androidarchitecture.data.model.ApiResponse
import com.example.androidarchitecture.data.model.Status
import com.example.androidarchitecture.data.model.User
import com.example.androidarchitecture.data.model.request.SignInRequest
import com.example.androidarchitecture.data.model.response.SignInResponse
import io.reactivex.Single
import java.io.File

class MockService {

    private val accessToken = "JJAS92UEIDKAXSG8W"
    private var user: User? = null
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
}