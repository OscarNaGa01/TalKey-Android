package com.example.talkey_android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.talkey_android.R
import com.example.talkey_android.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var popupWindow: PopupWindow? = null

    //    val args: ProfileFragmentArgs by navArgs()
    private var state: ProfileState = ProfileState.ShowProfile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

//        val isNew = args.isNew
//
//        if (isNew){
//            state = states[1]
//        }

        toolBarConfiguration()
        buttonConfiguration()



        return binding.root
    }

    private fun buttonConfiguration() {
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
//            requireActivity().onBackPressed()
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


    override fun onDestroy() {
        super.onDestroy()
        popupWindow?.dismiss()
    }


}