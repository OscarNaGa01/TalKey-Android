package com.example.talkey_android.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.model.users.UserProfileModel
import com.example.talkey_android.data.domain.use_cases.users.GetProfileUseCase
import com.example.talkey_android.data.domain.use_cases.users.SetOnlineUseCase
import com.example.talkey_android.data.domain.use_cases.users.UpdateProfileUseCase
import com.example.talkey_android.data.domain.use_cases.users.UploadImgUseCase
import com.example.talkey_android.databinding.FragmentProfileBinding
import com.example.talkey_android.ui.profile.popup.PopUpFragment
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class ProfileFragment : Fragment(), PopUpFragment.OnButtonClickListener {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileFragmentViewModel =
        ProfileFragmentViewModel(
            GetProfileUseCase(), SetOnlineUseCase(),
            UpdateProfileUseCase(), UploadImgUseCase()
        )
    private var popupWindow: PopupWindow? = null

    //    val args: ProfileFragmentArgs by navArgs()
    private lateinit var token: String
    private var state: ProfileState = ProfileState.ShowProfile
    private lateinit var imageUri: Uri
    private val cameraContract = registerForActivityResult(ActivityResultContracts.TakePicture()) {

    }
    private val galleryContract = registerForActivityResult(ActivityResultContracts.GetContent()) {

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

//        val isNew = args.isNew
//        token = args.token
        token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjE3NCIsImlhdCI6MTcwOTcyMDg4MywiZXhwIjoxNzEyMzEyODgzfQ.dGiM2NuUk9nEluZx_c0QlK6GeSfeEf_BRd-aqlQsReQ"
        imageUri = createUri()
//
//        if (isNew){
//            state = states[1]
//        }

        toolBarConfiguration()
        buttonConfiguration()



        observeViewModel()
        viewModel.getProfile(token)



        return binding.root
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.userProfile.collect { user ->
                Log.d("TAG", user.toString())
                setData(user)
            }
        }
    }

    private fun setData(user: UserProfileModel) {
        with(binding) {
            tvNickname.text = user.nick
            tvLogin.text = user.login
        }

        if (user.online) {
            binding.ivStatus.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.statusOnline
                )
            )
        } else {
            binding.ivStatus.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.statusOffline
                )
            )
        }

        Log.d("TAG", user.avatar)
        Glide.with(requireContext())
            .load("https://mock-movilidad.vass.es/chatvass/api/${user.avatar}")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.perfil)
            .into(binding.imgProfile)
    }

    private fun buttonConfiguration() {
        binding.ivStatus.setOnClickListener {
            val showPopUp = PopUpFragment(this, true)
            showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
        }

        binding.ivImageEdit.setOnClickListener {
            val showPopUp = PopUpFragment(this, false)
            showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
        }

        binding.btnAccept.setOnClickListener {
            when (state) {
                is ProfileState.ShowProfile -> { //Edit profile
                    showToPassword()
                }

                is ProfileState.EditProfile -> { //Cancel edit
                    editToShow()
                }

                is ProfileState.ChangePassword -> { //Cancel password change
                    passwordToShow()
                }
            }
        }
    }


    private fun toolBarConfiguration() {
        val toolbar: Toolbar = binding.toolBar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back_arrow_white)

        toolbar.setNavigationOnClickListener {

            // Your custom action here
//            findNavController().popBackStack()
        }



        binding.actionButton.setOnClickListener {
            toolbarSwitcher()
        }
    }

    private fun toolbarSwitcher() {
        when (state) {
            is ProfileState.ShowProfile -> { //Edit profile
                showToEdit()
            }

            is ProfileState.EditProfile -> { //Cancel edit
                editToShow()
            }

            is ProfileState.ChangePassword -> { //Cancel password change
                passwordToShow()
            }
        }
    }


    private fun showToEdit() {
        state = ProfileState.EditProfile
        binding.actionButton.setImageResource(R.drawable.x_white)
        with(binding) {
            etNickname.visibility = View.VISIBLE
            tvNickname.visibility = View.GONE
            tvLogin.visibility = View.GONE
            ivStatus.visibility = View.GONE
            ivImageEdit.visibility = View.VISIBLE
            btnAccept.text = getString(R.string.save)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            false
        )
    }

    private fun editToShow() {
        state = ProfileState.ShowProfile
        binding.actionButton.setImageResource(R.drawable.editar_white)
        with(binding) {
            etNickname.visibility = View.GONE
            tvNickname.visibility = View.VISIBLE
            tvLogin.visibility = View.VISIBLE
            ivStatus.visibility = View.VISIBLE
            ivImageEdit.visibility = View.GONE
            btnAccept.text = getString(R.string.change_password)
            etNickname.setText("")
            toolBar.setNavigationIcon(R.drawable.back_arrow_white)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
    }

    private fun passwordToShow() {
        state = ProfileState.ShowProfile
        binding.actionButton.setImageResource(R.drawable.editar_white)
        with(binding) {
            etPassword.visibility = View.GONE
            etPasswordConfirm.visibility = View.GONE
            tvNicknameLabel.text = getString(R.string.hint_nick)
            tvLoginLabel.text = getString(R.string.log_in_button)
            tvNickname.visibility = View.VISIBLE
            tvLogin.visibility = View.VISIBLE
            ivStatus.visibility = View.VISIBLE
            btnAccept.text = getString(R.string.change_password)
            etPassword.setText("")
            etPasswordConfirm.setText("")
            toolBar.setNavigationIcon(R.drawable.back_arrow_white)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
    }

    private fun showToPassword() {
        state = ProfileState.ChangePassword
        binding.actionButton.setImageResource(R.drawable.editar_white)
        with(binding) {
            etPassword.visibility = View.VISIBLE
            etPasswordConfirm.visibility = View.VISIBLE
            tvNicknameLabel.text = getString(R.string.hint_password)
            tvLoginLabel.text = getString(R.string.hint_confirm_password)
            tvNickname.visibility = View.GONE
            tvLogin.visibility = View.GONE
            ivStatus.visibility = View.GONE
            btnAccept.text = getString(R.string.save)
            etPassword.setText("")
            etPasswordConfirm.setText("")
            toolBar.setNavigationIcon(R.drawable.back_arrow_white)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
    }

    private fun createUri(): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val image = File(requireActivity().filesDir, "avatar_$timestamp.png")
        return FileProvider.getUriForFile(
            requireContext(),
            "com.example.talkey_android.FileProvider",
            image
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        popupWindow?.dismiss()
    }

    override fun onCameraClick() {
        Log.d("TAG", "Camera")
    }

    override fun onGalleryClick() {
        Log.d("TAG", "Gallery")
    }

    override fun switchOnline() {
        Log.d("TAG", "Online")
        viewModel.setOnline(true)
    }

    override fun switchOffline() {
        Log.d("TAG", "Offline")
        viewModel.setOnline(false)
    }


}