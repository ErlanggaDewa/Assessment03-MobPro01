package org.d3if1053.hitungzakatpenghasilan.ui.hitung

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if1053.hitungzakatpenghasilan.db.ZakatDao
import org.d3if1053.hitungzakatpenghasilan.db.ZakatEntity
import org.d3if1053.hitungzakatpenghasilan.model.ZakatModel

class HitungViewModel(val database: ZakatDao) :
    ViewModel() {
    var zakatModel: ZakatModel = ZakatModel()

    init {
        Log.i("HitungViewModel", "HitungViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("HitungViewModel", "HitungViewModel destroyed!")
    }

    fun isPayZakat(
        hargaEmas: String,
        penghasilan: String,
        bonus: String,
        savingStatus: Boolean
    ): Boolean {
        val nisab = (hargaEmas.toFloat() * 85) / 12;
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
}