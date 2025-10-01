package com.example.week3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btnsignin: Button
    lateinit var btnlogin: Button
    lateinit var editTextText3: EditText
    lateinit var editTextTextPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnsignin = findViewById(R.id.btnsignin)
        btnlogin = findViewById(R.id.btnlogin)
        editTextText3 = findViewById(R.id.editTextText3)
        editTextTextPassword = findViewById(R.id.editTextTextPassword)
        btnsignin.setOnClickListener(this)
        btnlogin.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnlogin -> {
                if (editTextText3.text.toString() == "SUT" && editTextTextPassword.text.toString() == "123") {
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Login fail", Toast.LENGTH_SHORT).show()
                    editTextText3.setText("")
                    editTextTextPassword.setText("")
                }

            }

            R.id.btnsignin -> {
                Toast.makeText(this, "Sign in", Toast.LENGTH_SHORT).show()
            }
        }
    }
    }





