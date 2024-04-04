package com.example.talkey_android.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.talkey_android.MainActivity
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.use_cases.users.LoginBiometricUseCase
import com.example.talkey_android.data.domain.use_cases.users.LoginUseCase
import com.example.talkey_android.data.domain.use_cases.users.RegisterUseCase
import com.example.talkey_android.data.utils.EncryptDecryptManager
import com.example.talkey_android.data.utils.Prefs
import com.example.talkey_android.data.utils.Token
import com.example.talkey_android.databinding.FragmentLogInBinding
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.regex.Pattern


class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private var isLogin: Boolean = true
    private val logInFragmentViewModel: LogInFragmentViewModel =
        LogInFragmentViewModel(RegisterUseCase(), LoginUseCase(), LoginBiometricUseCase())
    private lateinit var mainActivity: MainActivity

    private lateinit var prefs: Prefs
    private var isLoading = false
    private var isTryingBiometricAccess: Boolean = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.notNotificationAdvise),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        mainActivity = requireActivity() as MainActivity
        prefs = Prefs(requireContext())
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initListeners()
        observeViewModel()

        requestPostNotificationPermission()

        //biometric----------------------------------------------
        binding.btnFingerPrint.setOnClickListener {
            if (!isLoading) {
                isTryingBiometricAccess = true
                checkDeviceBiometric()
            }
        }
        //biometric----------------------------------------------


        return binding.root
    }

    private fun requestPostNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
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
                        isLoading = true
                    }

                    is LogInFragmentUiState.Success -> {
                        binding.progressBar.visibility = View.GONE

                        Token.setToken(uiState.userModel.token)

                        if (isTryingBiometricAccess) {

                            val encryptedToken =
                                EncryptDecryptManager.encrypt(uiState.userModel.token)

                            prefs.saveToken(encryptedToken)

                            doNavigation(uiState.userModel.id)
                        } else {
                            isTryingBiometricAccess = false
                            checkDeviceBiometric()
                            doNavigation(uiState.userModel.id)
                        }
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
            Toast.makeText(requireContext(), getString(R.string.user_exists), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoginError(error: ErrorModel) {
        if (error.message == "No token provided") {
            prefs.restartData()
            EncryptDecryptManager.removeKey()
            Toast.makeText(
                requireContext(),
                getString(R.string.manualLoginAdvise),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        }

        Log.i(">", error.errorCode)
        if (error.errorCode == "400") {
            setEditTextBackground(listOf(binding.etEmail))

        } else if (error.errorCode == "401") {
            setEditTextBackground(listOf(binding.etPassword))

        }
    }

    private fun doNavigation(userId: String) {
        if (!isLogin) {
            findNavController().navigate(
                LogInFragmentDirections.actionLogInFragmentToProfileFragment(
                    userId,
                    true
                )
            )
        } else {
            findNavController().navigate(
                LogInFragmentDirections.actionLogInFragmentToHomeFragment(
                    userId,
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
        editText.setSelection(editText.text.toString().length)
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
            setSignupToLoginView()
        }

    }

    private fun setSignupToLoginView() {
        with(binding) {
            etEmail.text?.clear()
            etNick.text?.clear()
            etPassword.text?.clear()
            etConfirmPassword.text?.clear()

            btnChange.text = getString(R.string.sign_up)
            btnAccept.text = getString(R.string.log_in)

            btnFingerPrint.visibility = View.VISIBLE

            etNick.visibility = View.GONE
            etConfirmPassword.visibility = View.GONE
            cbTermsConditions.visibility = View.GONE
            tvPasswordRequirements.visibility = View.GONE

            cbTermsConditions.isChecked = false
            isLogin = true

            setEditTextBackground(emptyList())
        }
    }

    private fun setLoginToSignupView() {
        with(binding) {
            etEmail.text?.clear()
            etPassword.text?.clear()

            btnChange.text = getString(R.string.log_in)
            btnAccept.text = getString(R.string.sign_up)

            etNick.visibility = View.VISIBLE
            etConfirmPassword.visibility = View.VISIBLE
            cbTermsConditions.visibility = View.VISIBLE

            btnFingerPrint.visibility = View.GONE

            isLogin = false

            setEditTextBackground(emptyList())
        }
    }

    private fun logIn() {
        with(binding) {
            if (etEmail.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()) {
                logInFragmentViewModel.postLogin(
                    LoginRequestModel(
                        etPassword.text.toString(),
                        etEmail.text.toString()
                    ),
                    requireContext()
                )
                setEditTextBackground(emptyList())
            } else {
                setEditTextBackground(listOf(etEmail, etPassword))
                Toast.makeText(
                    requireContext(),
                    getString(R.string.check_your_email_and_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun signUp() {
        with(binding) {
            if (isValidEmail(etEmail.text.toString()) && etNick.text.toString()
                    .isNotEmpty() && isValidPassword()
                && etPassword.text.toString() == etConfirmPassword.text.toString() && cbTermsConditions.isChecked
            ) {
                logInFragmentViewModel.postRegister(
                    RegisterRequestModel(
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        etNick.text.toString()
                    ),
                    requireContext()
                )

            } else if (etEmail.text.toString().isEmpty() && etNick.text.toString()
                    .isEmpty() && etPassword.text.toString()
                    .isEmpty() && etConfirmPassword.text.toString().isEmpty()
            ) {
                setEditTextBackground(listOf(etEmail, etNick, etPassword, etConfirmPassword))
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (!isValidEmail(etEmail.text.toString())) {
                setEditTextBackground(listOf(etEmail))
                Toast.makeText(
                    requireContext(),
                    getString(R.string.enter_a_valid_email), Toast.LENGTH_SHORT
                ).show()

            } else if (etNick.text.toString().isEmpty()) {
                setEditTextBackground(listOf(etNick))
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (!isValidPassword()) {
                setEditTextBackground(listOf(etPassword, etConfirmPassword))
                with(tvPasswordRequirements) {
                    visibility = View.VISIBLE
                    text = getString(R.string.password_requirements)
                }
                Toast.makeText(requireContext(), (R.string.invalid_password), Toast.LENGTH_SHORT)
                    .show()

            } else if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                tvPasswordRequirements.visibility = View.GONE
                setEditTextBackground(listOf(etPassword, etConfirmPassword))
                Toast.makeText(
                    requireContext(),
                    getString(R.string.passwords_dont_match),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (!cbTermsConditions.isChecked) {
                tvPasswordRequirements.visibility = View.GONE
                setEditTextBackground(emptyList())
                Toast.makeText(
                    requireContext(),
                    getString(R.string.accept_our_terms_and_conditions),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    private fun setEditTextBackground(currentEditText: List<EditText>) {
        with(binding) {
            etEmail.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)
            etNick.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)
            etPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)
            etConfirmPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)

        }
        currentEditText.forEach { editText ->
            editText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_error_background)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun isValidPassword(): Boolean {
        val patron =
            Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        return patron.matches(binding.etPassword.text.toString())
    }

    //biometric----------------------------------------------
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: PromptInfo

    @SuppressLint("SwitchIntDef")
    private fun checkDeviceBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {

                if (isTryingBiometricAccess) {
                    createBiometricListener()
                    createPromptInfo()
                    biometricPrompt.authenticate(promptInfo)

                } else if (binding.etEmail.text.toString() != prefs.getMail()) {

                    showDialogToSaveAccount()

                } else {

                    val encryptedToken = EncryptDecryptManager.encrypt(Token.getToken())
                    prefs.saveToken(encryptedToken)

                }
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                if (isTryingBiometricAccess) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.biometric_sensor_doesnt_exist),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                if (isTryingBiometricAccess) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.biometric_sensor_not_available),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                if (isTryingBiometricAccess) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.connection_with_sensor_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun createBiometricListener() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(requireContext(), errString, Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.fingerprint_mismatch), Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    val decryptedToken = EncryptDecryptManager.decrypt(prefs.getToken())
                    Token.setToken(decryptedToken)
                    logInFragmentViewModel.doBiometricLogin()
                }
            })
    }

    private fun createPromptInfo() {
        promptInfo = PromptInfo.Builder()
            .setTitle(getString(R.string.login_title))
            .setSubtitle(getString(R.string.use_fingerprint_login))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()
    }

    //biometric----------------------------------------------

    private fun showDialogToSaveAccount() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.unknown_account))
        builder.setMessage(getString(R.string.biometric_accunt_link_question))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->

            val encryptedToken = EncryptDecryptManager.encrypt(Token.getToken())

            prefs.saveToken(encryptedToken)
            prefs.saveMail(binding.etEmail.text.toString())
            Toast.makeText(
                requireContext(),
                getString(R.string.biometric_account_linked),
                Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }

        val dialog = builder.create()
        dialog.show()
    }
}
