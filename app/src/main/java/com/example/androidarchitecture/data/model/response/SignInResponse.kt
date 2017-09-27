package com.example.androidarchitecture.data.model.response

import com.example.androidarchitecture.data.model.User

data class SignInResponse(
        var accessToken: String,
        var user: User)