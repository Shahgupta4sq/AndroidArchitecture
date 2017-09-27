package com.example.androidarchitecture.data.model

data class User(
        var id: Long = 0L,
        var email: String,
        var firstName: String?,
        var lastName: String?,
        var bio: String?,
        var avatar: String?,
        var city: City?,
        var gender: Gender?)