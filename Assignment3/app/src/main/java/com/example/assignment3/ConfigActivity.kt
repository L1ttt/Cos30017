package com.example.assignment3

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout

class ConfigActivity : AppCompatActivity() {
    lateinit var btnBack: ImageButton
    lateinit var btnDelete: ImageButton

    lateinit var flexColor: FlexboxLayout
    lateinit var nameInput: EditText
    lateinit var pointInput: EditText
    lateinit var decreaseInput: EditText
    lateinit var increaseInput: EditText
    lateinit var btnSave: Button


    private var playerName: String? = null
    private var playerScore: Int = 0
    private var playerColor: Int = 0
    private var playerDecrease: Int = 0
    private var playerIncrease: Int = 0


    private var colorList = listOf(

        R.color.blue_500,
        R.color.blue_700,
        R.color.brown_500,
        R.color.indigo_500,
        R.color.green_500,
        R.color.green_700,
        R.color.orange_500,
        R.color.orange_700,
        R.color.red_500,
        R.color.pink_500,
        R.color.purple_500,
        R.color.purple_700,
        R.color.teal_500,
        R.color.cyan_500
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        playerName = intent.getStringExtra("Name")
        val originalName = intent.getStringExtra("Name")
        val playerIndex = intent.getIntExtra("PlayerIndex", 0)
        playerScore = intent.getIntExtra("Score", 0)
        playerColor = intent.getIntExtra("Color", 0)
        playerDecrease = intent.getIntExtra("Decrease", 0)
        playerIncrease = intent.getIntExtra("Increase", 0)
        btnBack = findViewById(R.id.btnBack)
        btnDelete = findViewById(R.id.btnDelete)
        flexColor = findViewById(R.id.flexColor)
        nameInput = findViewById(R.id.nameInput)
        pointInput = findViewById(R.id.pointInput)
        decreaseInput = findViewById(R.id.decreaseInput)
        increaseInput = findViewById(R.id.increaseInput)
        btnSave = findViewById(R.id.saveInput)




        btnBack.setOnClickListener {
            finish()
        }

        btnDelete.setOnClickListener {
                val resultIntent = Intent().apply {
                    putExtra("ACTION", "DELETE_PLAYER")
                    putExtra("PLAYER_INDEX", playerIndex)
                    putExtra("ORIGINAL_NAME", originalName)
                }
                setResult(RESULT_OK, resultIntent)
                finish()

        }

        nameInput.setText(playerName)
        pointInput.setText(playerScore.toString())
        decreaseInput.setText(playerDecrease.toString())
        increaseInput.setText(playerIncrease.toString())

        btnSave.setOnClickListener {
            if (nameInput.text.isNullOrEmpty() ||
                pointInput.text.isNullOrEmpty() ||
                decreaseInput.text.isNullOrEmpty() ||
                increaseInput.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                playerName = nameInput.text.toString()
                playerScore = pointInput.text.toString().toInt()
                playerDecrease = decreaseInput.text.toString().toInt()
                playerIncrease = increaseInput.text.toString().toInt()

                val resultIntent = Intent().apply {
                    putExtra("ACTION", "UPDATE_PLAYER")
                    putExtra("PLAYER_INDEX", playerIndex)
                    putExtra("ORIGINAL_NAME", originalName)
                    putExtra("PLAYER_NAME", playerName)
                    putExtra("PLAYER_SCORE", playerScore)
                    putExtra("PLAYER_COLOR", playerColor)
                    putExtra("PLAYER_DECREASE", playerDecrease)
                    putExtra("PLAYER_INCREASE", playerIncrease)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
        setColor()
    }
    private fun setColor(){
        for (colorResId in colorList) {
            btnSave.backgroundTintList = ContextCompat.getColorStateList(this, playerColor)
            val colorView = ImageView(this).apply {
                val color = ContextCompat.getColor(this@ConfigActivity, colorResId)
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(color)
                    if (colorResId == playerColor) {
                        setStroke(10, ContextCompat.getColor(this@ConfigActivity, R.color.white))
                    }
                }
                background = drawable
                layoutParams = FlexboxLayout.LayoutParams(
                    120,
                    120
                ).apply {

                    setMargins(10, 10, 10, 10)
                }

                setOnClickListener {
                    playerColor = colorResId
                    flexColor.removeAllViews()
                    setColor()
                }
            }


            flexColor.addView(colorView)

        }
    }




}


