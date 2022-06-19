package org.d3if1053.hitungzakatpenghasilan.ui.hitung

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if1053.hitungzakatpenghasilan.MainActivity
import org.d3if1053.hitungzakatpenghasilan.db.ZakatDao
import org.d3if1053.hitungzakatpenghasilan.db.ZakatEntity
import org.d3if1053.hitungzakatpenghasilan.model.GoldModel
import org.d3if1053.hitungzakatpenghasilan.model.ZakatModel
import org.d3if1053.hitungzakatpenghasilan.network.ApiStatus
import org.d3if1053.hitungzakatpenghasilan.network.GoldApi
import org.d3if1053.hitungzakatpenghasilan.network.UpdateWorker
import java.util.concurrent.TimeUnit

class HitungViewModel(val database: ZakatDao) :
    ViewModel() {
    var zakatModel: ZakatModel = ZakatModel()
    private val goldModel = MutableLiveData<GoldModel>()
    private val status = MutableLiveData<ApiStatus>()

    init {
        Log.i("HitungViewModel", "HitungViewModel created!")
        retrieveData()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("HitungViewModel", "HitungViewModel destroyed!")
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                val result = GoldApi.service.getPrice()
                goldModel.postValue(result)
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("HitungViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun isPayZakat(
        hargaEmas: String,
        penghasilan: String,
        bonus: String,
        savingStatus: Boolean
    ): Boolean {
        val nisab = (hargaEmas.toFloat() * 85) / 12
        var totalZakat: Long = 0

        if (savingStatus) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val dataZakat = ZakatEntity(
                        hargaEmas = hargaEmas.toLong(),
                        gajiBulanan = penghasilan.toLong(),
                        bonusGaji = bonus.toLong(),
                        totalZakat = totalZakat
                    )
                    database.insert(dataZakat)
                }
            }
        }

        if (penghasilan.toFloat() + bonus.toDouble() >= nisab) {
            totalZakat = ((penghasilan.toDouble() + bonus.toDouble()) * 0.025).toLong()
            zakatModel.totalZakat = totalZakat
            return true
        }

        return false
    }

    fun isNumeric(toCheck: String): Boolean {
        val regex = "[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(app).enqueueUniqueWork(
            MainActivity.CHANNEL_ID,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun getGoldData(): LiveData<GoldModel> = goldModel
    fun getStatus(): LiveData<ApiStatus> = status

}