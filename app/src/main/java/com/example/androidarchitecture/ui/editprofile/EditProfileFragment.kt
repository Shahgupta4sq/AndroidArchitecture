package com.example.androidarchitecture.ui.editprofile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.androidarchitecture.R
import com.example.androidarchitecture.data.model.Status
import com.example.androidarchitecture.data.model.User
import com.example.androidarchitecture.ui.base.BaseFragment
import com.example.androidarchitecture.ui.base.FragmentContainerActivity
import com.example.androidarchitecture.ui.home.HomeFragment
import com.example.androidarchitecture.ui.signin.SignInFragment
import com.example.androidarchitecture.util.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import javax.inject.Inject


class EditProfileFragment : BaseFragment() {

    companion object {
        private val REQUEST_GALLERY_IMAGE = 9001

        @JvmStatic
        fun newIntent(context: Context): Intent {
            return FragmentContainerActivity.newIntent(context, EditProfileFragment::class.java)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: EditProfileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_edit_profile, container, false)

        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.title = getString(R.string.edit_profile)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar?.setNavigationOnClickListener { finish() }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getStatus().observe(this, Observer { handleStatus(it) })
        viewModel.getState().observe(this, Observer { handleState(it) })
        viewModel.getUser().observe(this, Observer { setUser(it) })
        viewModel.getAvatar().observe(this, Observer { setAvatar(it) })

        sdvAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GALLERY_IMAGE)
        }
        etFirstName.addTextChangedListener(firstNameTextChangeListener)
        etLastName.addTextChangedListener(lastNameTextChangeListener)
        etBio.addTextChangedListener(bioTextChangeListener)
        etCity.setOnClickListener { showCitySelectionDialog() }
        etGender.setOnClickListener { showGenderSelectionDialog() }

        btnSave.setOnClickListener { viewModel.saveProfile() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GALLERY_IMAGE -> viewModel.handleImage(data)
        }
    }

    private fun handleStatus(status: Status?) {
        when (status) {
            Status.INVALID_ACCESS_TOKEN, Status.INVALID_USER -> {
                Toast.makeText(activity, "There's an issue with your account, please sign-in again", Toast.LENGTH_SHORT).show()
                val intent = SignInFragment.newIntent(activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity.finish()
            }
            Status.EMPTY_FIRST_NAME -> Toast.makeText(activity, "Please enter your first name!", Toast.LENGTH_SHORT).show()
            Status.EMPTY_LAST_NAME -> Toast.makeText(activity, "Please enter your last name", Toast.LENGTH_SHORT).show()
            Status.EMPTY_CITY -> Toast.makeText(activity, "Please choose your home city", Toast.LENGTH_SHORT).show()
            Status.INVALID_URI -> Toast.makeText(activity, "Unable to load the photo", Toast.LENGTH_SHORT).show()
            Status.SUCCESS -> {
                startActivity(HomeFragment.newIntent(activity))
                activity.finish()
            }
            Status.SERVER_CONNECTION_ERROR -> Toast.makeText(activity, "Unable to connect to server, please try again!", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(activity, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()

        }
    }

    private fun handleState(state: EditProfileState?) {
        if (state?.isCityDialogShown == true) {
            showCitySelectionDialog()
            return
        }
        if (state?.isGenderDialogShown == true) {
            showGenderSelectionDialog()
            return
        }
    }

    private fun setUser(user: User?) {
        user?.let {
            etFirstName.setText(it.firstName)
            etLastName.setText(it.lastName)
            etBio.setText(it.bio)
            etEmail.setText(it.email)
            etCity.setText(it.city?.name)
            etGender.setText(it.gender?.toString())
        }
    }

    private fun setAvatar(avatar: String?) {
        sdvAvatar.setImageURI(avatar)
    }

    private fun showCitySelectionDialog() {
        viewModel.setCityDialogState(true)
        val dialog = AlertDialog.Builder(activity)
                .setTitle(R.string.edit_profile_select_city)
                .setItems(R.array.select_city, { dialog, which ->
                    val cities = activity.resources.getTextArray(R.array.select_city)
                    viewModel.setCity(which + 1, cities[which] as String?)
                })
                .create()
        dialog.show()
        dialog.setOnDismissListener { viewModel.setCityDialogState(false) }
    }

    private fun showGenderSelectionDialog() {
        viewModel.setGenderDialogState(true)
        val dialog = AlertDialog.Builder(activity)
                .setTitle(R.string.edit_profile_select_gender)
                .setItems(R.array.select_gender, { dialog, which ->
                    val gender = activity.resources.getTextArray(R.array.select_gender)
                    viewModel.setGender(gender[which] as String?)
                })
                .create()
        dialog.show()
        dialog.setOnDismissListener { viewModel.setGenderDialogState(false) }
    }


    private val firstNameTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            viewModel.setFirstName(p0?.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    private val lastNameTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            viewModel.setLastName(p0?.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    private val bioTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            viewModel.setBio(p0?.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }
}