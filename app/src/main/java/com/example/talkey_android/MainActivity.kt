package com.example.talkey_android

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.talkey_android.data.constants.Constants.PLATFORM
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.use_cases.PostRegisterUseCase
import com.example.talkey_android.ui.LogInFragmentViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mViewModel = LogInFragmentViewModel(PostRegisterUseCase())
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screenSplash.setKeepOnScreenCondition { false }

        observeViewModel()

        mViewModel.postRegister(
            RegisterRequestModel(
                "yakisoba",
                "yakisoba",
                "yakisoba",
                PLATFORM
            )
        )

    }

    private fun observeViewModel() {
        val tvTry = findViewById<TextView>(R.id.tvTry)
        lifecycleScope.launch {
            mViewModel.register.collect {
                tvTry.text = it.success.toString()
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
    }
}