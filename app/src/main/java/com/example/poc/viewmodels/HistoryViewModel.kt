package com.example.poc.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poc.model.HistoryItem
import com.example.poc.database.HistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyDao: HistoryDao) : ViewModel() {

    fun getAllHistory(): LiveData<List<HistoryItem>> {
        return historyDao.getAllHistory()
    }

    fun insertHistoryItem(historyItem: HistoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            historyDao.insert(historyItem)
            Log.d("aram ","historyList"+ historyItem)
        }
    }
}
