package com.example.poc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryItem(
    val expression: String,
    val result: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
