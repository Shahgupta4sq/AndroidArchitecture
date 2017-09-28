package com.example.androidarchitecture.ui.signin

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.androidarchitecture.R
import com.example.androidarchitecture.data.model.Status
import com.example.androidarchitecture.ui.base.BaseFragment
import com.example.androidarchitecture.ui.base.FragmentContainerActivity
import com.example.androidarchitecture.ui.editprofile.EditProfileFragment
import com.example.androidarchitecture.ui.home.HomeFragment
import com.example.androidarchitecture.util.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject

class SignInFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return FragmentContainerActivity.newIntent(context, SignInFragment::class.java)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SignInViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SignInViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_sign_in, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getStatus().observe(this, Observer { handleStatus(it) })

        etEmail.addTextChangedListener(emailTextChangeListener)
        etPassword.addTextChangedListener(passwordTextChangeListener)

        btnSignIn.setOnClickListener { viewModel.signIn() }
    }

    private fun handleStatus(status: Status?) {
        when (status) {
            Status.EMPTY_EMAIL -> Toast.makeText(activity, "Please enter email!", Toast.LENGTH_SHORT).show()
            Status.INVALID_EMAIL -> Toast.makeText(activity, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            Status.EMPTY_PASSWORD -> Toast.makeText(activity, "Please enter password!", Toast.LENGTH_SHORT).show()
            Status.INVALID_PASSWORD -> Toast.makeText(activity, "Password should be of minimum 6 characters", Toast.LENGTH_SHORT).show()
            Status.SIGN_UP -> {
                startActivity(EditProfileFragment.newIntent(activity))
                activity.finish()
            }
            Status.SIGN_IN -> {
                startActivity(HomeFragment.newIntent(activity))
                activity.finish()
            }
            Status.INVALID_CREDENTIALS -> Toast.makeText(activity, "Either email or password is incorrect", Toast.LENGTH_SHORT).show()
            Status.SERVER_CONNECTION_ERROR -> Toast.makeText(activity, "Unable to connect to server, please try again!", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(activity, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
        }
    }

    private val emailTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            viewModel.setEmail(p0?.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    private val passwordTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            viewModel.setPassword(p0?.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }
}