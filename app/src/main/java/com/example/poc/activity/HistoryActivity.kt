package com.example.poc.activity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.poc.R
import com.example.poc.viewmodelfactory.HistoryViewModelFactory
import com.example.poc.database.AppDatabase
import com.example.poc.viewmodels.HistoryViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val historyDao = AppDatabase.getDatabase(this).historyDao()
        val historyViewModelFactory = HistoryViewModelFactory(historyDao)
       // val allHistory: LiveData<List<HistoryItem>> = historyRepository.allHistory

        historyViewModel = ViewModelProvider(this, historyViewModelFactory).get(HistoryViewModel::class.java)
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf()
        )

        val listView: ListView = findViewById(R.id.historyListView)
        listView.adapter = adapter

        historyViewModel.getAllHistory().observe(this, Observer { historyList ->
            historyList?.let {
                val historyItemsTextList = it.map { historyItem ->
                    "${historyItem.expression} = ${historyItem.result}"
                }
                Log.d("aram ","historyList"+ historyItemsTextList);
                adapter.clear()

                adapter.addAll(historyItemsTextList)
                adapter.notifyDataSetChanged()
            }
        })
    }
}
