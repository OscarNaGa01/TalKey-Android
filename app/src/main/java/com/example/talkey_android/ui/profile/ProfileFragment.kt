package com.example.talkey_android.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.talkey_android.data.utils.Utils
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

    //    val args: ProfileFragmentArgs by navArgs()
    private lateinit var token: String
    private var isNew: Boolean = false
    private var state: ProfileState = ProfileState.ShowProfile
    private var myUser: UserProfileModel? = null

    private val cropActivityResultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.changeCurrentAvatar(Utils.handleCropResult(result))
//            viewModel.handleCropResult(result)
        }
    private var imageUri: Uri? = null
    private var finalImageUri: Uri? = null

    private val cameraContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->

            Log.d("URIREADER", imageUri.toString())
            if (success) {
                imageUri?.let { uri ->
                    Utils.cropImage(uri, cropActivityResultContract)
                }
            } else {
                //TODO("Errores camara")
            }
        }

    private val galleryContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        if (uri != null) {
            val destUri = createUri()
            Utils.copyImageToUri(uri, destUri, requireContext().contentResolver)
            Utils.cropImage(destUri, cropActivityResultContract)
        } else {
            // TODO: Handle accordingly
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

//        val isNew = args.isNew

//        token = args.token
        token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjE2NiIsImlhdCI6MTcwOTczNTM5MSwiZXhwIjoxNzEyMzI3MzkxfQ.U9TpyrvWd3VuHO3MRZPfkXxQcieeW2-sggJvyMVIWSM"
        Log.d("TAG", imageUri.toString())
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
        //TODO("Errores observers")
        lifecycleScope.launch {
            viewModel.getProfile.collect { user ->
                Log.d("TAG", user.toString())
                myUser = user
                setData(myUser!!)
            }
        }

        lifecycleScope.launch {
            viewModel.getProfileError.collect { error ->
                Log.d("TAG", "l> Error: ${error.message}")
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.selectedNewAvatar.collect { uri ->
                if (state is ProfileState.EditProfile && uri != null) {
                    finalImageUri = uri
                    Glide.with(requireContext())
                        .load(uri)
                        .into(binding.imgProfile)
                }
            }
        }

    }

    private fun setData(user: UserProfileModel) {
        with(binding) {
            tvNickname.text = user.nick
            tvLogin.text = user.login
            etNickname.setText(user.nick)
        }

        if (user.online) {
            statusOnline()
        } else {
            statusOfflinree()
        }


        Log.d("TAG", user.avatar)
        Glide.with(requireContext())
            .load("https://mock-movilidad.vass.es/${user.avatar}")
            //.load("https://mock-movilidad.vass.es/chatvass/api/${user.avatar}")
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
                is ProfileState.ShowProfile -> { //Change password
                    showToPassword()
                }

                is ProfileState.EditProfile -> { //Confirm edit
                    if (imageUri != null && myUser != null) {
                        viewModel.saveData(
                            myUser!!.password, binding.etNickname.text.toString(),
                            Utils.uriToFile(requireContext(), imageUri!!)
                        )
                    }
                    editToShow()
                }

                is ProfileState.ChangePassword -> { //Confirm password change
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
            //TODO("Back toolbar")
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
                myUser.let {
                    setData(myUser!!)
                }
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
            ivStatus.visibility = View.VISIBLE
            ivImageEdit.visibility = View.GONE
            btnAccept.text = getString(R.string.change_password)
            etNickname.setText("")
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
        binding.toolBar.setNavigationIcon(R.drawable.back_arrow_white)


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
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
        binding.toolBar.setNavigationIcon(R.drawable.back_arrow_white)
    }

    private fun showToPassword() {
        state = ProfileState.ChangePassword
        binding.actionButton.setImageResource(R.drawable.x_white)
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
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            false
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

    private fun statusOnline() {
        binding.ivStatus.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), R.color.statusOnline
            )
        )
    }

    private fun statusOfflinree() {
        binding.ivStatus.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), R.color.statusOffline
            )
        )
    }


    override fun onCameraClick() {
        Log.d("TAG", "Camera")
        imageUri = createUri()
        cameraContract.launch(imageUri)
    }

    override fun onGalleryClick() {
        Log.d("TAG", "Gallery")
        galleryContract.launch("image/*")
    }

    override fun switchOnline() {
        Log.d("TAG", "Online")
        statusOnline()
        viewModel.setOnline(true)
    }

    override fun switchOffline() {
        Log.d("TAG", "Offline")
        statusOfflinree()
        viewModel.setOnline(false)
    }


}