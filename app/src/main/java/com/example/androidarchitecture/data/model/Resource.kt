package com.example.androidarchitecture.data.model

data class Resource<T>(
        var status: Status,
        var data: T,
        var message: String? = null) {

    companion object {
        @JvmStatic
        fun <T> loading(data: T?): Resource<T?> {
            return Resource(Status.LOADING, data)
        }
    }
}