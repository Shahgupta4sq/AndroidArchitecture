package com.example.androidarchitecture.ui.splash

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.example.androidarchitecture.R
import com.example.androidarchitecture.ui.base.BaseActivity
import com.example.androidarchitecture.ui.signin.SignInFragment
import com.example.androidarchitecture.util.ViewModelFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
    }
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        disposable = Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (viewModel.isSignedIn()) {
                    } else {
                        startActivity(SignInFragment.newIntent(this))
                        finish()
                    }
                }
    }

    override fun onStop() {
        disposable?.dispose()
        super.onStop()
    }
}
