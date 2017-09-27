package com.example.androidarchitecture.ui.splash

import android.os.Bundle
import com.example.androidarchitecture.R
import com.example.androidarchitecture.ui.base.BaseActivity
import com.example.androidarchitecture.ui.signin.SignInFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    startActivity(SignInFragment.newIntent(this))
                }
    }
}
