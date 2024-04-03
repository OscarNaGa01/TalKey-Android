package com.example.talkey_android.ui.password_change

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.use_cases.users.UpdateProfileUseCase
import com.example.talkey_android.databinding.FragmentPasswordChangeBinding
import kotlinx.coroutines.launch

class PasswordChangeFragment : Fragment() {

    private lateinit var binding: FragmentPasswordChangeBinding
    private val viewModel: PasswordChangeFragmentViewModel =
        PasswordChangeFragmentViewModel(UpdateProfileUseCase())

    private val args: PasswordChangeFragmentArgs by navArgs()
    private lateinit var image: String
    private lateinit var nick: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordChangeBinding.inflate(inflater, container, false)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        image = args.image
        nick = args.nick

        setAvatar()
        observeViewModel()
        initListeners()

        manageBack()

        return binding.root
    }

    private fun setAvatar() {
        Glide.with(requireContext())
            .load("https://mock-movilidad.vass.es/${image}")
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.perfil)
            .into(binding.imgProfile)
    }

    private fun observeViewModel() {

        lifecycleScope.launch {
            viewModel.updateProfileSuccess.collect {
                Toast.makeText(requireContext(), R.string.password_updated, Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }
        }

        lifecycleScope.launch {
            viewModel.updateProfileError.collect {
                Toast.makeText(requireContext(), R.string.password_error, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            cancelButton.setOnClickListener {
                cancelButton()
            }

            chbShowPasswdText.setOnCheckedChangeListener { _, isChecked ->
                showOrHideText(isChecked, etPassword)
            }

            chbShowConfirmPasswdText.setOnCheckedChangeListener { _, isChecked ->
                showOrHideText(isChecked, etPasswordConfirm)
            }

            btnAccept.setOnClickListener {
                updatingPassword()
            }
        }
    }

    private fun cancelButton() {
        findNavController().popBackStack()
    }

    private fun updatingPassword() {
        with(binding) {
            if (isValidPassword() && etPassword.text.toString() == etPasswordConfirm.text.toString()) {
                setEditTextBackground(emptyList())
                tvPasswordRequirements.visibility = View.GONE
                viewModel.updateProfile(
                    etPassword.text.toString(), nick
                )

            } else if (!isValidPassword()) {
                setEditTextBackground(listOf(etPassword, etPasswordConfirm))
                with(tvPasswordRequirements) {
                    visibility = View.VISIBLE
                    text = getString(R.string.password_requirements)
                }
                Toast.makeText(
                    requireContext(),
                    getString(R.string.invalid_password),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (etPassword.text.toString() != etPasswordConfirm.text.toString()) {
                setEditTextBackground(listOf(etPassword, etPasswordConfirm))
                binding.tvPasswordRequirements.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    getString(R.string.passwords_dont_match),
                    Toast.LENGTH_SHORT
                ).show()

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

    private fun isValidPassword(): Boolean {
        val patron =
            Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        return patron.matches(binding.etPassword.text.toString())
    }

    private fun setEditTextBackground(currentEditText: List<EditText>) {
        with(binding) {
            etPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)
            etPasswordConfirm.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)

        }
        currentEditText.forEach { editText ->
            editText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_error_background)
        }
    }

    private fun manageBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
    }


}