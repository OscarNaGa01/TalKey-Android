package com.example.talkey_android.ui.login

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.talkey_android.R
import com.example.talkey_android.data.constants.Constants.PLATFORM
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.use_cases.users.LoginUseCase
import com.example.talkey_android.data.domain.use_cases.users.RegisterUseCase
import com.example.talkey_android.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private var isLogin: Boolean = true
    private val logInFragmentViewModel: LogInFragmentViewModel =
        LogInFragmentViewModel(RegisterUseCase(), LoginUseCase())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        binding.ivBackground.setColorFilter(Color.argb(50, 0, 0, 0), PorterDuff.Mode.SRC_OVER)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        with(binding) {
            btnChange.setOnClickListener {
                setLoginSignupView(isLogin)
            }
            btnAccept.setOnClickListener {
                setLoginSignupAction(isLogin)
            }
        }
    }

    private fun setLoginSignupAction(login: Boolean) {
        if (isLogin) {
//            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
            Toast.makeText(requireContext(), "Log in", Toast.LENGTH_SHORT).show()
        } else {
            logInFragmentViewModel.postRegister(
                RegisterRequestModel(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etNick.text.toString(),
                    PLATFORM,
                    ""
                )
            )
            findNavController().navigate(
                LogInFragmentDirections.actionLogInFragmentToProfileFragment(
                    binding.etEmail.text.toString(),
                    "",
                    true
                )
            )
        }
    }

    private fun setLoginSignupView(login: Boolean) {
        with(binding) {
            if (login) {
                etConfirmPassword.visibility = View.VISIBLE
                etNick.visibility = View.VISIBLE
                btnChange.text = getString(R.string.log_in_button)
                btnAccept.text = getString(R.string.sign_up_button)
                isLogin = false
            } else {
                etConfirmPassword.visibility = View.GONE
                etNick.visibility = View.GONE
                btnChange.text = getString(R.string.sign_up_button)
                btnAccept.text = getString(R.string.log_in_button)
                isLogin = true
            }
        }
    }
}