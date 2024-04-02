package com.example.talkey_android.ui.profile.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.talkey_android.R
import com.example.talkey_android.databinding.FragmentPopUpBinding


class PopUpFragment(private val listener: OnButtonClickListener, private val isStatus: Boolean) :
    DialogFragment() {

    private lateinit var binding: FragmentPopUpBinding

    interface OnButtonClickListener {
        fun onCameraClick()
        fun onGalleryClick()
        fun switchOnline()
        fun switchOffline()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopUpBinding.inflate(inflater, container, false)

        listeners(isStatus)

        return binding.root
    }

    private fun listeners(isStatus: Boolean) {
        if (isStatus) {
            with(binding) {
                onlinePhoto.setOnClickListener {
                    listener.switchOnline()
                    dismiss()
                }
                offlineGallery.setOnClickListener {
                    listener.switchOffline()
                    dismiss()
                }
            }
        } else {
            with(binding) {
                ivOnline.visibility = View.GONE
                ivOffline.visibility = View.GONE
                tvOnlinePhoto.text = getString(R.string.camera_button)
                tvOfflineGallery.text = getString(R.string.gallery_button)
                onlinePhoto.setOnClickListener {
                    listener.onCameraClick()
                    dismiss()
                }
                offlineGallery.setOnClickListener {
                    listener.onGalleryClick()
                    dismiss()
                }
            }
        }

    }


}