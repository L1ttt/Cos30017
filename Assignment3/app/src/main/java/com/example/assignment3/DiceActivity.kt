package com.example.assignment3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class DiceActivity : AppCompatActivity() {
    private lateinit var header : TextView
   private lateinit var diceTextView: TextView
   private lateinit var totalTextView: TextView
   private lateinit var diceForm : LinearLayout

   private lateinit var faceInput : EditText
   private lateinit var diceInput : EditText

    private lateinit var btnBack: LinearLayout

    private lateinit var btnSetting: LinearLayout

    private var dice = ""

    private var face = 6
    private var diceCount = 1
    private var total = 0

    private var result = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)

        dice = intent.getStringExtra("dice").toString()
        header = findViewById(R.id.header)
        totalTextView = findViewById(R.id.total)
        diceForm = findViewById(R.id.diceForm)
        diceTextView = findViewById(R.id.dice)
        faceInput = findViewById(R.id.face)
        diceInput = findViewById(R.id.num)
        faceInput.setText(face.toString())
        diceInput.setText(diceCount.toString())

        if(dice == "dice"){
            header.text = "Dice"
            diceForm.visibility = View.VISIBLE
            totalTextView.visibility = View.VISIBLE
        }
        else{
            header.text = "Who go first?"
            diceForm.visibility = View.GONE
            totalTextView.visibility = View.GONE
        }
        diceTextView.setOnClickListener {

            if(dice == "dice"){
                result = ""
                face = faceInput.text.toString().toInt()
                diceCount = diceInput.text.toString().toInt()
                if(face == 0 ||face == null || diceCount == 0 || diceCount == null){
                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                rollDice()
                diceTextView.text = result
                totalTextView.text = "Total: "+total.toString()
            }
            else{
                randomPlayer()
                diceTextView.text = result

            }
        }

        btnBack = findViewById(R.id.nav_score)
        btnSetting = findViewById(R.id.nav_setting)

        btnBack.setOnClickListener {
            finish()
        }
        btnSetting.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("ACTION", "NAV_SETTING")
                putExtra("dice", dice)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
    private fun rollDice() {
        var rollList = mutableListOf<Int>()
        for (i in 1..diceCount) {
            var ran = Random.nextInt(1, face + 1)
            rollList.add(ran)
        }
        total = rollList.sum()
        for (i in 0 until rollList.size) {
            result += rollList[i].toString() + "  "
        }

    }
    private fun randomPlayer(){


        val players = intent.getSerializableExtra("players") as? ArrayList<Player>

        if (!players.isNullOrEmpty()) {
            val randomPlayer = players.random()
            result = randomPlayer.name
        } else {
            result = "No Players"
        }
    }
    }

