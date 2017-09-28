package com.example.androidarchitecture.data.remote

import android.net.Uri
import com.example.androidarchitecture.data.model.ApiResponse
import com.example.androidarchitecture.data.model.Status
import com.example.androidarchitecture.data.model.User
import com.example.androidarchitecture.data.model.request.SignInRequest
import com.example.androidarchitecture.data.model.response.SignInResponse
import io.reactivex.Single
import java.io.File
import javax.inject.Inject

class UserRemoteDataStore @Inject constructor(private val mockService: MockService) {

    fun signIn(signInRequest: SignInRequest): Single<ApiResponse<SignInResponse?>?> {
        return mockService.signIn(signInRequest)
    }

    fun getProfile(accessToken: String?, id: Long): Single<ApiResponse<User?>?> {
        return mockService.getProfile(accessToken, id)
    }

    fun saveProfile(accessToken: String?, user: User): Single<ApiResponse<User?>?> {
        return mockService.updateProfile(accessToken, user)
    }

    fun uploadImage(uri: Uri): Single<Pair<Status, String?>> {
        return mockService.uploadImage(uri)
    }
}