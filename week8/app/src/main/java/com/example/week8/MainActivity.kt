package com.example.week8

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    lateinit var txtDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        callThread()
        txtDisplay = findViewById(R.id.txtDisplay)

    }

    fun callThread() {
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {
                val result1 = Thread1()
                val result2 = Thread2()
                val result = result1 + result2
                Log.d("T1", "Result: $result")



            }
            Log.d("T1", "Total time: $executionTime")
            withContext(Main) {
                txtDisplay.text = " Total time: $executionTime"
            }

        }
    }


    suspend fun Thread1(): String {
        delay(1000)

        return "Thread1"
    }

    suspend fun Thread2(): String {
        delay(1700)
        return "Thread2"
    }
}