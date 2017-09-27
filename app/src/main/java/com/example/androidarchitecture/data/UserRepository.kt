package com.example.androidarchitecture.data

import android.net.Uri
import com.example.androidarchitecture.data.local.UserLocalDataStore
import com.example.androidarchitecture.data.model.ApiResponse
import com.example.androidarchitecture.data.model.Resource
import com.example.androidarchitecture.data.model.Status
import com.example.androidarchitecture.data.model.User
import com.example.androidarchitecture.data.model.request.SignInRequest
import com.example.androidarchitecture.data.remote.UserRemoteDataStore
import com.example.androidarchitecture.util.ResourceManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userLocalDataStore: UserLocalDataStore,
                                         private val userRemoteDataStore: UserRemoteDataStore) {

    fun signIn(signInRequest: SignInRequest): Single<Status> {
        return userRemoteDataStore.signIn(signInRequest)
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    it?.data?.let {
                        userLocalDataStore.signIn(it.accessToken, it.user)
                    }
                }
                .flatMap { apiResponse ->
                    Single.just(apiResponse.status)
                }
    }

    fun isSignedIn() = userLocalDataStore.isSignedIn()

    fun getProfile(): Flowable<Resource<User?>> {
        return Flowable.create({ emitter ->
            object : ResourceManager<User?, User?>(emitter) {
                override fun getLocalData(): Flowable<User?> {
                    return userLocalDataStore.getProfile()
                }

                override fun getRemoteData(): Single<ApiResponse<User?>?> {
                    return userRemoteDataStore.getProfile(userLocalDataStore.getAccessToken(), userLocalDataStore.getId())
                }

                override fun saveRemoteResult(item: User?) {
                    item?.let { userLocalDataStore.saveProfile(it) }
                }
            }
        }, BackpressureStrategy.BUFFER)
    }

    fun saveProfile(user: User, photoUri: Uri?): Single<Status> {
        val saveProfile = userRemoteDataStore.saveProfile(userLocalDataStore.getAccessToken(), user)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { response ->
                    response?.data?.let {
                        userLocalDataStore.saveProfile(it)
                    }
                }
                .flatMap { apiResponse -> Single.just(apiResponse.status) }

        return photoUri?.let {
            userRemoteDataStore.uploadImage(it)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        if (it.first == Status.SUCCESS && !it.second.isNullOrBlank()) {
                            user.avatar = it.second
                            saveProfile
                        } else {
                            Single.just(it.first)
                        }
                    }
        } ?: run {
            saveProfile
        }
    }

    fun signOut() = userLocalDataStore.signOut()
}