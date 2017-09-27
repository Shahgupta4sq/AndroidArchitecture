package com.example.androidarchitecture.data.remote

import com.example.androidarchitecture.data.model.ApiResponse
import com.example.androidarchitecture.data.model.User
import com.example.androidarchitecture.data.model.request.SignInRequest
import com.example.androidarchitecture.data.model.response.SignInResponse
import io.reactivex.Single
import javax.inject.Inject

class UserRemoteDataStore @Inject constructor(private val mockService: MockService) {

    fun signIn(signInRequest: SignInRequest): Single<ApiResponse<SignInResponse?>?> {
        return mockService.signIn(signInRequest)
    }

    fun getProfile(accessToken: String, id: Long): Single<ApiResponse<User?>?> {
        return mockService.getProfile(accessToken, id)
    }

    fun saveProfile(accessToken: String, user: User): Single<ApiResponse<User?>?> {
        return mockService.updateProfile(accessToken, user)
    }
}