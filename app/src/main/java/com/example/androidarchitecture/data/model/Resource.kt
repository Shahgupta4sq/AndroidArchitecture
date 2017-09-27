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

        @JvmStatic
        fun <T> custom(status: Status?, data: T?, message: String?): Resource<T?> {
            return Resource(status ?: Status.ERROR, data, message)
        }
    }
}