package com.example.talkey_android.ui.login

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talkey_android.R
import com.example.talkey_android.data.constants.Constants.PLATFORM
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.use_cases.users.LoginUseCase
import com.example.talkey_android.data.domain.use_cases.users.RegisterUseCase
import com.example.talkey_android.databinding.FragmentLogInBinding
import kotlinx.coroutines.launch
import java.util.regex.Pattern


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
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            logInFragmentViewModel.user.collect { user ->
                if (user.token.isNotEmpty() && !isLogin) {
                    findNavController().navigate(
                        LogInFragmentDirections.actionLogInFragmentToProfileFragment(
                            user.id,
                            user.token,
                            true
                        )
                    )

                } else if (user.token.isNotEmpty() && isLogin) {
                    findNavController().navigate(
                        LogInFragmentDirections.actionLogInFragmentToHomeFragment(
                            user.id,
                            user.token
                        )
                    )
                }
            }
        }

        lifecycleScope.launch {
            logInFragmentViewModel.registerError.collect { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }
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
        if (login) {
            logIn()

        } else {
            signUp()
        }
    }

    private fun setLoginSignupView(signup: Boolean) {
        if (signup) {
            setLoginToSignupView()

        } else {
            setSetupToLoginView()
        }

    }

    private fun setSetupToLoginView() {
        with(binding) {
            etEmail.text?.clear()
            etNick.text?.clear()
            etPassword.text?.clear()
            etConfirmPassword.text?.clear()
            etConfirmPassword.visibility = View.GONE
            etNick.visibility = View.GONE
            cbTermsConditions.visibility = View.GONE
            cbTermsConditions.isChecked = false
            btnChange.text = getString(R.string.sign_up_button)
            btnAccept.text = getString(R.string.log_in_button)
            isLogin = true
        }
    }

    private fun setLoginToSignupView() {
        with(binding) {
            etEmail.text?.clear()
            etPassword.text?.clear()
            etConfirmPassword.visibility = View.VISIBLE
            etNick.visibility = View.VISIBLE
            cbTermsConditions.visibility = View.VISIBLE
            btnChange.text = getString(R.string.log_in_button)
            btnAccept.text = getString(R.string.sign_up_button)
            isLogin = false
        }
    }

    private fun logIn() {
        if (binding.etEmail.text.toString().isNotEmpty() && binding.etPassword.text.toString().isNotEmpty())
            lifecycleScope.launch {
                logInFragmentViewModel.postLogin(
                    LoginRequestModel(
                        binding.etPassword.text.toString(),
                        binding.etEmail.text.toString(),
                        PLATFORM,
                        ""
                    )
                )

            } else {
            Toast.makeText(requireContext(), "Check your email and password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp() {
        with(binding) {
            if (isValidEmail(etEmail.text.toString()) && etNick.text.toString().isNotEmpty() && isValidPassword()
                && etPassword.text.toString() == etConfirmPassword.text.toString() && cbTermsConditions.isChecked
            ) {
                lifecycleScope.launch {
                    logInFragmentViewModel.postRegister(
                        RegisterRequestModel(
                            etEmail.text.toString(),
                            etPassword.text.toString(),
                            etNick.text.toString(),
                            PLATFORM,
                            ""
                        )
                    )
                }

            } else if (etEmail.text.toString().isEmpty() && etNick.text.toString()
                    .isEmpty() && etPassword.text.toString()
                    .isEmpty() && etConfirmPassword.text.toString().isEmpty()
            ) {
                setEditTextBackground(listOf(etEmail, etNick, etPassword, etConfirmPassword))
                Toast.makeText(requireContext(), "Complete all fields", Toast.LENGTH_SHORT).show()

            } else if (!isValidEmail(etEmail.text.toString())) {
                setEditTextBackground(listOf(etEmail))
                Toast.makeText(requireContext(), "Enter a valid email", Toast.LENGTH_SHORT).show()

            } else if (etNick.text.toString().isEmpty()) {
                setEditTextBackground(listOf(etNick))
                Toast.makeText(requireContext(), "You need a nickname", Toast.LENGTH_SHORT).show()

            } else if (!isValidPassword()) {
                setEditTextBackground(listOf(etPassword, etConfirmPassword))
                Toast.makeText(requireContext(), "Invalid password", Toast.LENGTH_SHORT).show()

            } else if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                setEditTextBackground(listOf(etPassword, etConfirmPassword))
                Toast.makeText(requireContext(), "Passwords don't match", Toast.LENGTH_SHORT).show()

            } else if (!cbTermsConditions.isChecked) {
                setEditTextBackground(emptyList())
                Toast.makeText(requireContext(), "Accept our terms and conditions", Toast.LENGTH_SHORT).show()

            } else {
            }
        }
    }

    private fun setEditTextBackground(currentEditText: List<EditText>) {
        with(binding) {
            etEmail.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)
            etNick.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)
            etPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)
            etConfirmPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)

        }
        currentEditText.forEach { editText ->
            editText.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_error_background)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun isValidPassword(): Boolean {
        val patron = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        return patron.matches(binding.etPassword.text.toString())
    }
}