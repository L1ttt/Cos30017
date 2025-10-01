package com.example.asignment1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.Boolean
import kotlin.Int
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import java.util.Locale


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btnClimb: Button
    lateinit var btnFall: Button
    lateinit var btnReset: Button
    lateinit var textScore: TextView
    lateinit var btnLang: Button
    var score: Int = 0
    var fall: Boolean = false
    companion object {
        var selectedLanguage: String = "en"
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_FALL_STATE = "extra_fall_state"
    }
    //On create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra(EXTRA_SCORE)) {
            score = intent.getIntExtra(EXTRA_SCORE, 0)
        }
        if (intent.hasExtra(EXTRA_FALL_STATE)) {
            fall = intent.getBooleanExtra(EXTRA_FALL_STATE, false)
        }

        btnClimb = findViewById(R.id.btnClimb)
        btnFall = findViewById(R.id.btnFall)
        btnReset = findViewById(R.id.btnReset)
        textScore = findViewById(R.id.textScore)
        btnLang = findViewById(R.id.btnLang)

        updateScoreDisplay()
        //Set onclick
        btnClimb.setOnClickListener(this)
        btnFall.setOnClickListener(this)
        btnReset.setOnClickListener(this)
        textScore.setOnClickListener(this)
        updateLanguageButton()
    }
    //Language button onclick
    private fun updateLanguageButton() {
        if (selectedLanguage == "en") {
            btnLang.setOnClickListener { changeLanguage("vi") }
        } else if (selectedLanguage == "vi") {
            btnLang.setOnClickListener { changeLanguage("en") }
        }
    }
    //Score color
    private fun updateScoreDisplay() {
        textScore.text = score.toString()
        when {
            score <= 3 -> textScore.setTextColor(Color.BLUE)
            score > 3 && score < 12 -> textScore.setTextColor(Color.GREEN)
            score >= 12 -> textScore.setTextColor(Color.RED)
            else -> textScore.setTextColor(Color.BLACK)
        }
    }

    //Change string file selected
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(applyLanguage(newBase))
        Log.d("MainActivity", "attachBaseContext")
    }
    //Change language
    private fun changeLanguage(language: String) {
        selectedLanguage = language
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(EXTRA_SCORE, score)
        intent.putExtra(EXTRA_FALL_STATE, fall)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) // Clear back stack
        startActivity(intent)
        finish()
        Log.d("MainActivity", " $language")
    }
    //Apply language
    private fun applyLanguage(context: Context): Context {
        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        Log.d("MainActivity", " $selectedLanguage")
        return context.createConfigurationContext(config)
    }
    //Save state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score", score)
        outState.putBoolean("fall", fall)
        Log.d("MainActivity", "onSaveInstanceState: score=$score, fall=$fall")
    }
    //Restore state
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score = savedInstanceState.getInt("score")
        fall = savedInstanceState.getBoolean("fall")
        updateScoreDisplay() // Use the helper method
        Log.d("MainActivity", "onRestoreInstanceState: score=$score, fall=$fall")
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            //Climb
            R.id.btnClimb, R.id.textScore -> {
                if (fall) {
                    Toast.makeText(this, "Reset to continue", Toast.LENGTH_SHORT).show()
                } else {
                    if (score < 3) {
                        score++
                    }
                    else if (score < 9) {
                        score += 2
                    }
                    else if (score < 18) {
                        score += 3
                    }
                    updateScoreDisplay()
                }
            }
            //Fall
            R.id.btnFall -> {
                if (fall || score == 18) {
                    Toast.makeText(this, "Reset to continue", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (score < 3) {
                        score = 0
                    }
                    else if (score < 18) {
                        score -= 3
                    }
                    fall = true
                    updateScoreDisplay()

                }
            }
            //Reset
            R.id.btnReset -> {
                score = 0
                fall = false
                updateScoreDisplay()

            }
        }
    }
}
