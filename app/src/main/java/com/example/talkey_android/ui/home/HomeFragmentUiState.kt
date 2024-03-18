package com.example.talkey_android.ui.home

sealed class HomeFragmentUiState {
    data object Loading : HomeFragmentUiState()
    data class Success(val dataList: List<Any>) : HomeFragmentUiState()
    data class Error(val msg: String) : HomeFragmentUiState()
}