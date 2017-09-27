package com.example.androidarchitecture.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.androidarchitecture.R

class FragmentContainerActivity : BaseActivity() {

    companion object {
        private val TAG = this::class.java.simpleName
        private val EXTRA_FRAGMENT_NAME = "$TAG.EXTRA_FRAGMENT_NAME"
        private val EXTRA_THEME = "$TAG.EXTRA_THEME"

        @JvmStatic
        fun newIntent(context: Context, fragmentClass: Class<*>, theme: Int? = null): Intent {
            val intent = Intent(context, FragmentContainerActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentClass.name)
            intent.putExtra(EXTRA_THEME, theme)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        intent?.getIntExtra(EXTRA_THEME, -1)?.takeIf { it != -1 }?.let {
            setTheme(it)
            intent.removeExtra(EXTRA_THEME)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        val fragmentManager = supportFragmentManager
        var fragment = fragmentManager.findFragmentById(R.id.flFragmentContainer)

        if (fragment == null) {
            intent?.getSerializableExtra(EXTRA_FRAGMENT_NAME)?.let { fragmentName ->
                intent.removeExtra(EXTRA_FRAGMENT_NAME)
                fragment = Fragment.instantiate(this, fragmentName.toString(), intent.extras)
                fragmentManager
                        .beginTransaction()
                        .add(R.id.flFragmentContainer, fragment)
                        .commit()
            } ?: run { finish() }
        }
    }
}