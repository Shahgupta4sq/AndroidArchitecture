package com.example.androidarchitecture.data.model

data class ApiResponse<T>(
        var status: Status,
        var data: T,
        var message: String? = null)