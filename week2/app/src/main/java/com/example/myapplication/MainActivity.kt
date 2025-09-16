package com.example.myapplication



import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var plus: Button
    lateinit var minus: Button
    lateinit var multiply: Button
    lateinit var divide: Button
    lateinit var num1: EditText
    lateinit var num2: EditText
    lateinit var result: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        plus = findViewById(R.id.plus)
        minus = findViewById(R.id.minus)
        multiply = findViewById(R.id.multiply)
        divide = findViewById(R.id.divide)
        num1 = findViewById(R.id.num1)
        num2 = findViewById(R.id.num2)
        result = findViewById(R.id.result)
        divide.setOnClickListener(this)
        multiply.setOnClickListener(this)
        minus.setOnClickListener(this)
        plus.setOnClickListener(this)
    }



    override fun onClick(v: View?)
    {
        val text1 = num1.text.toString().toDouble()
        val text2 = num2.text.toString().toDouble()
        val result = result


        when (v?.id) {
            R.id.plus -> {
                result.setText((text1 + text2).toString())
            }
            R.id.minus -> {
                result.setText((text1 - text2).toString())
            }
            R.id.multiply -> {
                result.setText((text1 * text2).toString())
            }
            R.id.divide -> {
                result.setText((text1 / text2).toString())
            }
            else -> {
                result.setText("0")
            }

        }
    }
}

