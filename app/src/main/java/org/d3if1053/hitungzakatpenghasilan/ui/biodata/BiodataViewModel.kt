package org.d3if1053.hitungzakatpenghasilan.ui.biodata

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if1053.hitungzakatpenghasilan.model.UserModel
import org.d3if1053.hitungzakatpenghasilan.network.ApiStatus
import org.d3if1053.hitungzakatpenghasilan.network.UserApi

class BiodataViewModel : ViewModel() {
    private val status = MutableLiveData<ApiStatus>()

    private val userModel: MutableLiveData<UserModel> by lazy {
        MutableLiveData<UserModel>().also {
            retrieveData()
        }
    }

    fun getUser(): LiveData<UserModel> {
        return userModel
    }


    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                val result = UserApi.service.getUser()
                userModel.postValue(result)
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("BiodataViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun getStatus(): LiveData<ApiStatus> = status
}