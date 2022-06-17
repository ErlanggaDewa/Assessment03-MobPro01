package org.d3if1053.hitungzakatpenghasilan.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if1053.hitungzakatpenghasilan.db.ZakatDao

class HistoryViewModelFactory(
    private val dataSource: ZakatDao
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}