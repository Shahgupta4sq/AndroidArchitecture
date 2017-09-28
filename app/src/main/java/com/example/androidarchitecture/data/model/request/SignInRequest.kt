package com.example.androidarchitecture.data.model.request

data class SignInRequest(
        var email: String?,
        var password: String?){

    constructor(): this(null, null)
}