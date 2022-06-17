package org.d3if1053.hitungzakatpenghasilan.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if1053.hitungzakatpenghasilan.db.ZakatDao

class HistoryViewModel(
    val database: ZakatDao
) : ViewModel() {
    val data = database.getData()

    fun hapusData() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            database.clearData()
        }
    }
}