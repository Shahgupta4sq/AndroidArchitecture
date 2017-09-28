package com.example.androidarchitecture.data.model

enum class Status {
    SUCCESS,
    LOADING,
    ERROR,
    SERVER_CONNECTION_ERROR,
    SIGN_UP,
    SIGN_IN,
    INVALID_CREDENTIALS,
    INVALID_ACCESS_TOKEN,
    INVALID_USER,
    IMAGE_UPLOAD_FAILED,

    //validation error
    EMPTY_EMAIL,
    INVALID_EMAIL,
    EMPTY_PASSWORD,
    INVALID_PASSWORD
}