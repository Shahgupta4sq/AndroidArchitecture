package com.example.androidarchitecture.ui.signin

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.util.Patterns
import com.example.androidarchitecture.data.UserRepository
import com.example.androidarchitecture.data.model.Status
import com.example.androidarchitecture.data.model.request.SignInRequest
import com.example.androidarchitecture.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val status = SingleLiveEvent<Status>()
    private val signIn = SignInRequest()
    private var disposable: Disposable? = null

    fun setEmail(email: String?) {
        signIn.email = email
    }

    fun setPassword(password: String?) {
        signIn.password = password
    }

    fun getStatus(): LiveData<Status> {
        return status
    }

    fun signIn() {
        validateData().takeIf { it }?.let {
            disposable = userRepository.signIn(signIn)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        status.value = it
                    }, {
                        status.value = Status.SERVER_CONNECTION_ERROR
                    })
        }
    }

    private fun validateData(): Boolean {
        if (signIn.email.isNullOrBlank()) {
            status.value = Status.EMPTY_EMAIL
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(signIn.email).matches()) {
            status.value = Status.INVALID_EMAIL
            return false
        }
        if (signIn.password.isNullOrBlank()) {
            status.value = Status.EMPTY_PASSWORD
            return false
        }
        if (signIn.password?.length ?: 0 < 6) {
            status.value = Status.INVALID_PASSWORD
            return false
        }
        return true
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}