package com.example.talkey_android.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.talkey_android.MainActivity
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.messages.SendMessageUseCase
import com.example.talkey_android.data.utils.Utils
import com.example.talkey_android.databinding.FragmentChatBinding
import com.example.talkey_android.databinding.ItemProfileImageStatusChatBinding
import com.example.talkey_android.ui.chat.adapter.ChatAdapter
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var profileImageStatusBinding: ItemProfileImageStatusChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayout: LinearLayoutManager
    private val args: ChatFragmentArgs by navArgs()
    private val chatFragmentViewModel: ChatFragmentViewModel =
        ChatFragmentViewModel(SendMessageUseCase(), GetListMessageUseCase(), GetListChatsUseCase())
    private lateinit var mainActivity: MainActivity
    private var visibleItemCount = 0
    private var pastVisibleItems = 0
    private var totalItemCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        profileImageStatusBinding = ItemProfileImageStatusChatBinding.bind(binding.root)
        mainActivity = requireActivity() as MainActivity
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        observeViewModel()
        initListeners()
        getMessageList(false)
    }

    private fun initListeners() {
        with(binding) {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        findNavController().popBackStack()
                    }
                })
            ivSend.setOnClickListener {
//                resetLinearLayoutValues(rvChat)
                sendMessage()
            }
            rvChat.setOnClickListener {
                mainActivity.hideKeyBoard()
            }
            swipeToRefresh.setOnRefreshListener {
//                resetLinearLayoutValues(rvChat)
                getMessageList(true)
                swipeToRefresh.isRefreshing = false
            }
        }
    }

//    private fun resetLinearLayoutValues(linearLayout: RecyclerView) {
//        linearLayout.post {
//            linearLayout.scrollToPosition(0) // Esto asegura que la vista esté en la posición inicial
//            linearLayout.layoutManager?.apply {
//                scrollToPosition(0) // Asegura que el layout manager esté en la posición inicial
//            }
//            linearLayout.clearOnScrollListeners() // Elimina cualquier listener de desplazamiento existente
//            linearLayout.viewTreeObserver.dispatchOnPreDraw() // Actualiza la vista antes de acceder a sus valores
//            linearLayout.apply {
//                visibleItemCount = 0
//                totalItemCount = 0
//                pastVisibleItems = 0
//            }
//        }
//    }

    private fun sendMessage() {
        with(binding) {
            if (etMessage.text.toString().isNotEmpty()) {
                chatFragmentViewModel.sendMessage(
                    args.token,
                    args.idChat,
                    args.idUser,
                    etMessage.text.toString()
                )
                etMessage.text?.clear()
                rvChat.scrollToPosition(rvChat.top)
            }
        }
    }

    private fun getMessageList(isSentMessage: Boolean) {
        chatFragmentViewModel.getContactData(args.token, args.idChat, args.idUser)
        chatFragmentViewModel.getMessages(args.token, args.idChat, isSentMessage)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.contact.collect { userData ->

                binding.tvName.text = userData.targetNick

                Glide.with(requireContext())
                    .load(userData.targetAvatar)
                    .error(R.drawable.image)
                    .into(profileImageStatusBinding.ivProfile)

                if (userData.targetOnline) {
                    profileImageStatusBinding.ivStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.statusOnline
                        )
                    )

                } else {
                    profileImageStatusBinding.ivStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.statusOffline
                        )
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.message.collect { messages ->
                Utils.showDateOnce(messages)
                chatAdapter.updateList(messages)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.setMessageError.collect { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.getListMessageError.collect { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            chatFragmentViewModel.getListChatsError.collect { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView() {
        chatAdapter = ChatAdapter(arrayListOf(), args.idUser)
        linearLayout = LinearLayoutManager(context)

        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = linearLayout
        }
        setupPagination()
    }

//    private fun setupPagination() {
//        var loading = true
//        binding.rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0) {
//                    visibleItemCount = linearLayout.childCount
//                    totalItemCount = linearLayout.itemCount
//                    pastVisibleItems = linearLayout.findFirstVisibleItemPosition()
//                    if (loading && visibleItemCount + pastVisibleItems >= totalItemCount) {
//                        loading = false
//                        getMessageList(false)
//                    } else if (visibleItemCount + pastVisibleItems < totalItemCount && !loading) {
//                        loading = true
//                    }
//                }
//            }
//        }
//        )
//    }

    private fun setupPagination() {
        binding.rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager? = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager?.findLastCompletelyVisibleItemPosition() == recyclerView.adapter?.itemCount?.minus(1)) {
                    getMessageList(false)
                }
            }
        })
    }
}