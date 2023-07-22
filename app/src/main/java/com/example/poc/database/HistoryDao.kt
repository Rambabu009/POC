package com.example.poc.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.poc.model.HistoryItem

@Dao
interface HistoryDao {
    @Insert
    fun insert(historyItem: HistoryItem)

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<HistoryItem>>
}
