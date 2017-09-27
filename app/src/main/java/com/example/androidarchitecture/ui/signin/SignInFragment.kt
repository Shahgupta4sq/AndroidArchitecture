package com.example.androidarchitecture.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidarchitecture.R
import com.example.androidarchitecture.ui.base.BaseFragment
import com.example.androidarchitecture.ui.base.FragmentContainerActivity

class SignInFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return FragmentContainerActivity.newIntent(context, SignInFragment::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}