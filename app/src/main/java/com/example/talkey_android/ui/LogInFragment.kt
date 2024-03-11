package com.example.talkey_android.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.talkey_android.R
import com.example.talkey_android.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private var isLogin: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        binding.ivBackground.setColorFilter(Color.argb(50, 0, 0, 0), PorterDuff.Mode.SRC_OVER)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        with(binding) {
            btnChange.setOnClickListener {
                if (isLogin) {
                    etConfirmPassword.visibility = View.VISIBLE
                    etNick.visibility = View.VISIBLE
                    btnChange.text = getString(R.string.log_in_button)
                    btnAccept.text = getString(R.string.sign_up_button)
                    isLogin = false
                } else {
                    etConfirmPassword.visibility = View.GONE
                    etNick.visibility = View.GONE
                    btnChange.text = getString(R.string.sign_up_button)
                    btnAccept.text = getString(R.string.log_in_button)
                    isLogin = true
                }
            }
            btnAccept.setOnClickListener {
                if (isLogin) {
//                    findNavController().navigate(LogInFragmentDirections.actionLoginToHome())
                    Toast.makeText(requireContext(), "Log in", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Sign up", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}