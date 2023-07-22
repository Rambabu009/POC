package com.example.poc.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
//import com.example.poc.HistoryRoomData.*
import com.example.poc.R
import com.example.poc.viewmodelfactory.HistoryViewModelFactory
import com.example.poc.database.AppDatabase
import com.example.poc.databinding.ActivityMainBinding
import com.example.poc.model.HistoryItem
import com.example.poc.restservices.MathService
import com.example.poc.restservices.RetrofitHelper
import com.example.poc.viewmodels.HistoryViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.btnSubmit.setOnClickListener(this)
        val historyDao = AppDatabase.getDatabase(this).historyDao()
        val historyButton: Button = findViewById(R.id.btnhistorytab) //ID of our button
        historyButton.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryActivity::class.java) // To get anotherActivity
            startActivity(intent)

        }
    }

    override fun onClick(v: View?) {
        val quotesApi = RetrofitHelper.getInstance().create(MathService::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val myString: String?
            myString = binding.mtinput.text.toString()
            val inputExpression = binding.mtinput.text.toString().trim()

            if (inputExpression.isEmpty()) {                // Show a toast indicating that the input is empty
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Please enter a valid expression", Toast.LENGTH_SHORT).show()
                }
            } else {
                quotesApi.myObjectById(myString).enqueue(object : Callback<String> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<String>, response: Response<String>) { //Retrofit response
                        if (response.isSuccessful) {
                            insertToDb(response, myString)
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) { // Retrofit failure case handling
                        Toast.makeText(
                            this@MainActivity,
                            "onFailure something went wrong at server end",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

            }
        }
    }

    private fun insertToDb(response: Response<String>, myString: String?) {
        val value: String? = response.body()
        Toast.makeText(this@MainActivity, "onResponse successful", Toast.LENGTH_SHORT).show()
        binding.txtview.setText("Result Expression : " + value.toString())
        binding.mtinput.setText("")
        binding.mtinput.focusable

        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = sdf.format(currentTime)
        val expressionWithTime = "Time $formattedTime  --> $myString"
        val historyItem =
            HistoryItem(expression = expressionWithTime, result = value.toString())
        val historyDao = AppDatabase.getDatabase(this@MainActivity).historyDao()
        val historyViewModelFactory = HistoryViewModelFactory(historyDao)
        val historyViewModel =
            ViewModelProvider(this@MainActivity, historyViewModelFactory).get(
                HistoryViewModel::class.java
            )
        historyViewModel.insertHistoryItem(historyItem)
    }
}