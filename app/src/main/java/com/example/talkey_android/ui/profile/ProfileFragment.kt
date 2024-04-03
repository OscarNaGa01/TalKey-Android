package com.example.talkey_android.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.talkey_android.MainActivity
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
import java.util.Locale

class ProfileFragment : Fragment(), PopUpFragment.OnButtonClickListener {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileFragmentViewModel =
        ProfileFragmentViewModel(
            GetProfileUseCase(), SetOnlineUseCase(),
            UpdateProfileUseCase(), UploadImgUseCase()
        )

    private val args: ProfileFragmentArgs by navArgs()
    private lateinit var id: String
    private var isNew: Boolean = false
    private var state: ProfileState = ProfileState.ShowProfile
    private var myUser: UserProfileModel? = null
    private lateinit var mainActivity: MainActivity

    private val cropActivityResultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.changeCurrentAvatar(Utils.handleCropResult(result))
            viewModel.setLoad(false)
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
                viewModel.setLoad(false)
            }
        }

    private val galleryContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val destUri = createUri()
                Utils.copyImageToUri(uri, destUri, requireContext().contentResolver)
                viewModel.setLoad(true)
                Utils.cropImage(destUri, cropActivityResultContract)
            } else {
                viewModel.setLoad(false)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        mainActivity = requireActivity() as MainActivity
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        id = args.id
        isNew = args.isNew

        Log.d("TAG", imageUri.toString())

        observeViewModel()
        viewModel.getProfile()

        toolBarConfiguration()
        initListeners()

        if (isNew) {
            state = ProfileState.EditProfile
            showToEdit()
        }

        manageBack(isNew)

        return binding.root
    }

    private fun observeViewModel() {
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

        lifecycleScope.launch {
            viewModel.updateProfileSuccess.collect {
                if (isNew) {
                    findNavController().navigate(
                        ProfileFragmentDirections.actionProfileFragmentToHomeFragment(id)
                    )
                } else {
                    Log.d("TAG", it.success.toString())
                    editToShow()
                    viewModel.getProfile()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateProfileError.collect { error ->
                if (error.errorCode == "401") {
                    setEditTextBackground(listOf(binding.etNickname))
                    Toast.makeText(requireContext(), R.string.user_exists, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.uploadImgError.collect {
                Toast.makeText(
                    requireContext(), getText(R.string.photo_upload_error), Toast.LENGTH_SHORT
                ).show()
                finalImageUri = null
                setData(myUser!!)
            }
        }

        lifecycleScope.launch {
            viewModel.setOnlineError.collect {
                Toast.makeText(
                    requireContext(), getText(R.string.status_update_error), Toast.LENGTH_SHORT
                ).show()
            }
        }

        lifecycleScope.launch {
            viewModel.loadStateFlow.collect { isLoading ->
                if (isLoading) {
                    switchToLoading(true)
                } else {
                    switchToLoading(false)
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
            statusOffline()
        }


        Log.d("TAG", user.avatar)
        Glide.with(requireContext())
            .load("https://mock-movilidad.vass.es/${user.avatar}")
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.perfil)
            .into(binding.imgProfile)
    }

    private fun toolBarConfiguration() {
        val toolbar: Toolbar = binding.toolBar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back_arrow_white)

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initListeners() {
        with(binding) {
            constraintLayout.setOnClickListener {
                mainActivity.hideKeyBoard()
            }

            ivStatus.setOnClickListener {
                displayPopUp(true)
            }

            ivImageEdit.setOnClickListener {
                displayPopUp(false)
            }

            editButton.setOnClickListener {
                showToEdit()
            }

            cancelButton.setOnClickListener {
                cancelButton()
            }

            btnAccept.setOnClickListener {
                if (isNew) {
                    updatingProfile()

                } else {
                    accpetSwitcher()
                }
            }
        }
    }


    private fun displayPopUp(isStatus: Boolean) {
        val showPopUp = PopUpFragment(this, isStatus)
        showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
    }

    private fun accpetSwitcher() {
        when (state) {
            is ProfileState.ShowProfile -> { //Change password
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToPasswordChangeFragment(
                        myUser!!.avatar,
                        myUser!!.nick
                    )
                )
            }

            is ProfileState.EditProfile -> { //Confirm edit
                updatingProfile()
            }
        }
    }

    private fun updatingProfile() {
        with(binding) {
            if (finalImageUri != null && myUser != null && etNickname.text.toString()
                    .isNotEmpty()
            ) {
                setEditTextBackground(emptyList())
                viewModel.saveData(
                    myUser!!.password, etNickname.text.toString(),
                    Utils.uriToFile(requireContext(), finalImageUri!!)
                )

            } else if (finalImageUri == null && myUser != null && etNickname.text.toString()
                    .isNotEmpty()
            ) {
                setEditTextBackground(emptyList())
                viewModel.saveData(
                    myUser!!.password, etNickname.text.toString(),
                    null
                )

            } else {
                Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT)
                    .show()
                setEditTextBackground(listOf(etNickname))
            }
        }
    }


    private fun cancelButton() {
        editToShow()
        setData(myUser!!)
        setEditTextBackground(emptyList())
    }

    private fun setEditTextBackground(currentEditText: List<EditText>) {
        with(binding) {
            etNickname.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)

        }
        currentEditText.forEach { editText ->
            editText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_error_background)
        }
    }

    private fun createUri(): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val image = File(requireActivity().filesDir, "avatar_$timestamp.png")
        return FileProvider.getUriForFile(
            requireContext(),
            "com.example.talkey_android.FileProvider",
            image
        )
    }

    private fun showToEdit() {
        state = ProfileState.EditProfile
        with(binding) {
            etNickname.visibility = View.VISIBLE
            ivImageEdit.visibility = View.VISIBLE
            ivImageEditBackground.visibility = View.VISIBLE

            if (!isNew) {
                cancelButton.visibility = View.VISIBLE
            }

            editButton.visibility = View.GONE
            tvNickname.visibility = View.GONE
            ivStatus.visibility = View.GONE
            ivStatusBackground.visibility = View.GONE

            btnAccept.text = getString(R.string.save)
            etNickname.setText(myUser!!.nick)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            false
        )
    }

    private fun editToShow() {
        state = ProfileState.ShowProfile
        with(binding) {
            editButton.visibility = View.VISIBLE
            tvNickname.visibility = View.VISIBLE
            ivStatus.visibility = View.VISIBLE
            ivStatusBackground.visibility = View.VISIBLE

            cancelButton.visibility = View.GONE
            etNickname.visibility = View.GONE
            ivImageEdit.visibility = View.GONE
            ivImageEditBackground.visibility = View.GONE

            btnAccept.text = getString(R.string.change_password)
            etNickname.setText("")
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
        binding.toolBar.setNavigationIcon(R.drawable.back_arrow_white)


    }

    private fun switchToLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                loadingBackground.visibility = View.VISIBLE
                loadingProgressBar.visibility = View.VISIBLE

                cancelButton.visibility = View.GONE
                btnAccept.visibility = View.GONE
                etNickname.visibility = View.GONE
                ivImageEdit.visibility = View.GONE
                ivImageEditBackground.visibility = View.GONE
            } else {
                cancelButton.visibility = View.VISIBLE
                btnAccept.visibility = View.VISIBLE
                etNickname.visibility = View.VISIBLE
                ivImageEdit.visibility = View.VISIBLE
                ivImageEditBackground.visibility = View.VISIBLE

                loadingBackground.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
            }
        }
    }

    private fun statusOnline() {
        binding.ivStatus.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), R.color.statusOnline
            )
        )
    }

    private fun statusOffline() {
        binding.ivStatus.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), R.color.statusOffline
            )
        )
    }


    override fun onCameraClick() {
        Log.d("TAG", "Camera")
        imageUri = createUri()
        viewModel.setLoad(true)
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
        statusOffline()
        viewModel.setOnline(false)
    }

    private fun manageBack(isNew: Boolean) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!isNew && state == ProfileState.ShowProfile) {
                        findNavController().popBackStack()
                    } else if (!isNew && state == ProfileState.EditProfile) {
                        editToShow()
                    } else {
                        // Do nothing
                    }
                }
            })
    }

}