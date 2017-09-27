package com.example.androidarchitecture.util

import com.example.androidarchitecture.data.model.ApiResponse
import com.example.androidarchitecture.data.model.Resource
import com.example.androidarchitecture.data.model.Status
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

abstract class ResourceManager<LocalType, RemoteType>(private val emitter: FlowableEmitter<Resource<LocalType?>>) {

    init {
        //send down loading state to show progress bar
        emitter.onNext(Resource.loading(null))
        //send down stale data from local source with loading status
        val localDataDisposable = getLocalData()
                .map { Resource.loading(it) }
                .subscribe { emitter.onNext(it) }
        //Dispose local source stream after getting remote data
        getRemoteData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe({ response ->
                    localDataDisposable.dispose()
                    saveRemoteResult(response?.data)
                    getLocalData()
                            .map { Resource.custom(response?.status, it, response?.message) }
                            .subscribe { emitter.onNext(it) }
                }, {
                    emitter.onNext(Resource(Status.SERVER_CONNECTION_ERROR, null))
                })
    }

    protected abstract fun getLocalData(): Flowable<LocalType?>

    protected abstract fun getRemoteData(): Single<ApiResponse<RemoteType?>?>

    protected abstract fun saveRemoteResult(item: RemoteType?)
}