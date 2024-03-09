package com.example.talkey_android

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.talkey_android.data.constants.Constants.PLATFORM
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.use_cases.PostLoginUseCase
import com.example.talkey_android.data.domain.use_cases.PostRegisterUseCase
import com.example.talkey_android.ui.LogInFragmentViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mViewModel = LogInFragmentViewModel(
        PostRegisterUseCase(),
        PostLoginUseCase()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screenSplash.setKeepOnScreenCondition { false }

        observeViewModel()

        /*mViewModel.postRegister(
            RegisterRequestModel(
                "wasabi",
                "wasabi",
                "wasabi",
                PLATFORM
            )
        )*/
        mViewModel.postLogin(LoginRequestModel("wasabi", "wasabi", PLATFORM))

    }

    private fun observeViewModel() {
        val tvId = findViewById<TextView>(R.id.tvId)
        val tvNick = findViewById<TextView>(R.id.tvNick)
        val tvAvatar = findViewById<TextView>(R.id.tvAvatar)
        val tvOnline = findViewById<TextView>(R.id.tvOnline)
        val tvToken = findViewById<TextView>(R.id.tvToken)

        lifecycleScope.launch {
            mViewModel.user.collect {
                tvId.text = it.id
                tvNick.text = it.nick
                tvAvatar.text = it.avatar
                tvOnline.text = it.online.toString()
                tvToken.text = it.token
            }
        }


        lifecycleScope.launch {
            mViewModel.registerError.collect { error ->
                Toast.makeText(
                    this@MainActivity,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        lifecycleScope.launch {
            mViewModel.loginError.collect { error ->
                Toast.makeText(
                    this@MainActivity,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}