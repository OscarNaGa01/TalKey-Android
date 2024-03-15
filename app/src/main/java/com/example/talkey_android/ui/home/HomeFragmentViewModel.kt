package com.example.talkey_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.users.GetListProfilesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel(
    private val getListProfilesUseCase: GetListProfilesUseCase,
    private val getListChatsUseCase: GetListChatsUseCase
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserItemListModel>>(mutableListOf())
    val users: StateFlow<List<UserItemListModel>> = _users
    private val _getUsersListError = MutableSharedFlow<ErrorModel>()
    val getUsersListError: SharedFlow<ErrorModel> = _getUsersListError


    fun getUsersList(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val baseResponse = getListProfilesUseCase(token)

            when (baseResponse) {
                is BaseResponse.Success -> {
                    _users.emit(baseResponse.data.users)
                }

                is BaseResponse.Error -> {
                    _getUsersListError.emit(baseResponse.error)
                }
            }
        }
    }

}