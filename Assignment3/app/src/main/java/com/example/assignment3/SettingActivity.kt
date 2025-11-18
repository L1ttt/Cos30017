package com.example.assignment3
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
class SettingActivity : AppCompatActivity() {
    lateinit var btnWin: TextView
    lateinit var btnLose: TextView
    lateinit var btnTotal: TextView
    lateinit var btnIncrease: TextView
    lateinit var btnDecrease: TextView
    lateinit var btnNone: TextView
    lateinit var btnBack: LinearLayout
    private var sort: String? = null
    private var header: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        btnWin = findViewById(R.id.btnWin)
        btnLose = findViewById(R.id.btnLose)
        btnTotal = findViewById(R.id.btnTotal)
        btnIncrease = findViewById(R.id.btnIncrease)
        btnDecrease = findViewById(R.id.btnDecrease)
        btnNone = findViewById(R.id.btnNone)
        btnBack = findViewById(R.id.nav_score)
        sort = intent.getStringExtra("sort")
        header = intent.getStringExtra("header")

        btnWin.setOnClickListener {
            header = "win"
            activeBtn()

        }
        btnLose.setOnClickListener {
            header = "lose"
            activeBtn()
        }
        btnTotal.setOnClickListener {
            header = "total"
            activeBtn()
        }
        btnIncrease.setOnClickListener {
            sort = "increase"
            activeBtn()
        }
        btnDecrease.setOnClickListener {
            sort = "decrease"
            activeBtn()
        }
        btnNone.setOnClickListener {
            sort = "none"
            activeBtn()
        }
        btnBack.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("ACTION", "UPDATE_SETTING")
                putExtra("sort", sort)
                putExtra("header", header)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        activeBtn()

    }

    private fun activeBtn() {

        val defaultBackground = ContextCompat.getDrawable(this, R.drawable.button_list)
        btnIncrease.background = defaultBackground
        btnDecrease.background = defaultBackground
        btnNone.background = defaultBackground
        btnWin.background = defaultBackground
        btnLose.background = defaultBackground
        btnTotal.background = defaultBackground

        if (sort == "increase") {
            btnIncrease.background = ContextCompat.getDrawable(this, R.drawable.button_active)
        } else if (sort == "decrease") {
            btnDecrease.background = ContextCompat.getDrawable(this, R.drawable.button_active)
        } else if (sort == "none"){
            btnNone.background = ContextCompat.getDrawable(this, R.drawable.button_active)
        }
        if (header == "win"){
            btnWin.background = ContextCompat.getDrawable(this, R.drawable.button_active)
        }
        else if (header == "lose"){
            btnLose.background = ContextCompat.getDrawable(this, R.drawable.button_active)
        }
        else if (header == "total"){
            btnTotal.background = ContextCompat.getDrawable(this, R.drawable.button_active)


        }
    }

}