package com.example.simplehero.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplehero.utils.Event
import com.example.simplehero.utils.UIEvent

open class BaseViewModel : ViewModel() {
    val loading = MutableLiveData(false)
    val uiState = MutableLiveData<Event<UIEvent<Nothing>>>()
    val showStateInfo = MutableLiveData(false)
    val stateInfo = MutableLiveData<String>()

    fun setLoading(loading: Boolean) {
        this.loading.value = loading
    }

    fun setStateInfo(show: Boolean, message: String = "") {
        showStateInfo.value = show
        stateInfo.value = message
    }
}
