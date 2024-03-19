package com.example.talkey_android.ui.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talkey_android.MainActivity
import com.example.talkey_android.R
import com.example.talkey_android.data.constants.Constants.PLATFORM
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.UserModel
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
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        mainActivity = requireActivity() as MainActivity
        initListeners()
        observeViewModel()
        return binding.root
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            logInFragmentViewModel.uiState.collect { uiState ->
                when (uiState) {
                    is LogInFragmentUiState.Start -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is LogInFragmentUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is LogInFragmentUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        doNavigation(uiState.userModel)
                    }

                    is LogInFragmentUiState.LoginError -> {
                        binding.progressBar.visibility = View.GONE
                        showLoginError(uiState.errorModel)
                    }

                    is LogInFragmentUiState.RegisterError -> {
                        binding.progressBar.visibility = View.GONE
                        showRegisterError(uiState.errorModel)
                    }
                }
            }
        }
    }

    private fun showRegisterError(error: ErrorModel) {
        if (error.errorCode == "401") {
            setEditTextBackground(listOf(binding.etEmail))
            Toast.makeText(requireContext(), "User exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoginError(error: ErrorModel) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        if (error.errorCode == "400") {
            setEditTextBackground(listOf(binding.etEmail))

        } else if (error.errorCode == "401") {
            setEditTextBackground(listOf(binding.etPassword))
        }
    }

    private fun doNavigation(user: UserModel) {
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

    private fun initListeners() {
        with(binding) {

            chbShowConfirmPasswdText.setOnCheckedChangeListener { _, isChecked ->
                showOrHideText(isChecked, etConfirmPassword)
            }

            chbShowPasswdText.setOnCheckedChangeListener { _, isChecked ->
                showOrHideText(isChecked, etPassword)
            }

            constraintLayout.setOnClickListener {
                mainActivity.hideKeyBoard()
            }
            btnChange.setOnClickListener {
                setLoginSignupView(isLogin)
            }
            btnAccept.setOnClickListener {
                setLoginSignupAction(isLogin)
            }
        }
    }

    private fun showOrHideText(checked: Boolean, editText: AppCompatEditText) {
        if (checked) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
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
            setEditTextBackground(emptyList())
            isLogin = true
            btnFingerPrint.visibility = View.VISIBLE
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
            setEditTextBackground(emptyList())
            isLogin = false
            btnFingerPrint.visibility = View.GONE
        }
    }

    private fun logIn() {
        with(binding) {
            if (etEmail.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty())
                lifecycleScope.launch {
                    logInFragmentViewModel.postLogin(
                        LoginRequestModel(
                            etPassword.text.toString(),
                            etEmail.text.toString(),
                            PLATFORM,
                            ""
                        )
                    )
                    setEditTextBackground(emptyList())

                } else {
                setEditTextBackground(listOf(etEmail, etPassword))
                Toast.makeText(requireContext(), "Check your email and password", Toast.LENGTH_SHORT).show()
            }
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
                Toast.makeText(
                    requireContext(),
                    "Invalid password. It must be composed of 8 characters, a capital letter and a special character.",
                    Toast.LENGTH_LONG
                ).show()

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