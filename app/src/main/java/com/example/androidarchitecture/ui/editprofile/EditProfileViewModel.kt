package com.example.androidarchitecture.ui.editprofile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.net.Uri
import com.example.androidarchitecture.data.UserRepository
import com.example.androidarchitecture.data.model.City
import com.example.androidarchitecture.data.model.Gender
import com.example.androidarchitecture.data.model.Status
import com.example.androidarchitecture.data.model.User
import com.example.androidarchitecture.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val status = SingleLiveEvent<Status>()
    private val state = MutableLiveData<EditProfileState>()
    private val user = MutableLiveData<User>()
    private val avatar = MutableLiveData<String>()
    private var disposable: Disposable? = null

    init {
        state.value = EditProfileState()

        disposable = userRepository.getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    user.value = it.data
                    avatar.value = it.data?.avatar
                }
    }

    fun setFirstName(firstName: String?) {
        user.value?.firstName = firstName
    }

    fun setLastName(lastName: String?) {
        user.value?.lastName = lastName
    }

    fun setBio(bio: String?) {
        user.value?.bio = bio
    }

    fun setCity(cityId: Int?, cityName: String?) {
        if (cityId != null && cityId != 0 && !cityName.isNullOrBlank()) {
            user.value?.city = City(cityId, cityName!!)
            user.value = user.value
        }
    }

    fun setGender(gender: String?) {
        if (!gender.isNullOrBlank()) {
            user.value?.gender = Gender.valueOf(gender!!)
            user.value = user.value
        }
    }

    fun setCityDialogState(isCityDialogShown: Boolean) {
        state.value?.isCityDialogShown = isCityDialogShown
    }

    fun setGenderDialogState(isGenderDialogShown: Boolean) {
        state.value?.isGenderDialogShown = isGenderDialogShown
    }

    fun getStatus(): LiveData<Status> {
        return status
    }

    fun getState(): LiveData<EditProfileState> {
        return state
    }

    fun getUser(): LiveData<User> {
        return user
    }

    fun getAvatar(): LiveData<String> {
        return avatar
    }

    fun handleImage(intent: Intent?) {
        intent?.data?.let {
            avatar.value = it.toString()
        } ?: run { status.value = Status.INVALID_URI }
    }

    fun saveProfile() {
        validateData().takeIf { it }?.let {
            user.value?.let {
                var photoUri: Uri? = null
                if (avatar.value != user.value?.avatar) {
                    photoUri = Uri.parse(avatar.value)
                }
                disposable = userRepository.saveProfile(it, photoUri)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            status.value = it
                        }, {
                            status.value = Status.SERVER_CONNECTION_ERROR
                        })
            }
        }
    }

    private fun validateData(): Boolean {
        user.value?.let {
            if (it.firstName.isNullOrBlank()) {
                status.value = Status.EMPTY_FIRST_NAME
                return false
            }
            if (it.lastName.isNullOrBlank()) {
                status.value = Status.EMPTY_LAST_NAME
                return false
            }
            if (it.city == null || it.city?.name.isNullOrBlank()) {
                status.value = Status.EMPTY_CITY
                return false
            }
            return true
        } ?: run { return false }
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}