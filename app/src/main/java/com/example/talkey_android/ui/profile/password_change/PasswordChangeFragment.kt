package com.example.talkey_android.ui.profile.password_change

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.talkey_android.databinding.FragmentPasswordChangeBinding

class PasswordChangeFragment : Fragment() {

    private lateinit var binding: FragmentPasswordChangeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordChangeBinding.inflate(inflater, container, false)


        return binding.root
    }


}