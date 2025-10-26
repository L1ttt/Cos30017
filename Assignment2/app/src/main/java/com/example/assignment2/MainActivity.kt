package com.example.assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    // instrument list
    private val instrumentList = mutableListOf(
        Instrument("Guitar", 4f, "", 200, R.drawable.guitar, 0, "Guitar", "Standard", "Fender", "Black", "New"),
        Instrument("Keyboard", 5f, "", 250, R.drawable.keyboard, 0,"Keyboard", "Professional", "Yamaha", "White", "New"),
        Instrument("Drum Set", 4f, "", 300, R.drawable.drum, 0,"Drum", "Standard","Roland", "Black", "Old"),
    )

    //index
    private var currentIndex = 0
    private var credit = 3000


    // detail
    private val detailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                credit = result.data?.getIntExtra("updatedCredit", credit) ?: credit
                val updatedInstrument = result.data?.getParcelableExtra<Instrument>("updatedInstrument")
                if (updatedInstrument != null) {
                    instrumentList[currentIndex] = updatedInstrument
                    updateInstrument()
                }
            }
        }

            //id
            lateinit var itemImg: ImageView
            lateinit var creditText: TextView
            lateinit var itemName: TextView
            lateinit var itemRating: RatingBar
            lateinit var itemPrice: TextView
            lateinit var itemRadio: RadioGroup
            lateinit var itemBorrowed: TextView
            lateinit var btnPrevious: Button
            lateinit var btnBorrow: Button
            lateinit var btnNext: Button


            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)

                // Initialize views
                creditText = findViewById(R.id.credit)
                itemImg = findViewById(R.id.itemImg)
                itemName = findViewById(R.id.itemName)
                itemRating = findViewById(R.id.itemRating)
                itemPrice = findViewById(R.id.itemPrice)
                itemRadio = findViewById(R.id.itemRadio)
                itemBorrowed = findViewById(R.id.itemBorrowed)
                btnPrevious = findViewById(R.id.btnPrevious)
                btnBorrow = findViewById(R.id.btnBorrow)
                btnNext = findViewById(R.id.btnNext)
                //Radio


                // Set initial instrument
                updateInstrument()

                // pre/next btn
                btnPrevious.setOnClickListener {
                    if (currentIndex > 0) {
                        currentIndex--
                        updateInstrument()
                    } else {
                        Toast.makeText(this, "No previous instrument", Toast.LENGTH_SHORT).show()
                    }

                }
                btnNext.setOnClickListener {
                    if (currentIndex < instrumentList.size - 1) {
                        currentIndex++
                        updateInstrument()
                    } else {
                        Toast.makeText(this, "No next instrument", Toast.LENGTH_SHORT).show()
                    }
                }
                //Borrow btn
                btnBorrow.setOnClickListener {
                    val selectedType = when (itemRadio.checkedRadioButtonId) {
                        R.id.rbAcoustic -> "Acoustic"
                        R.id.rbElectric -> "Electric"
                        R.id.rbBass -> "Bass"
                        else -> null
                    }

                    if (selectedType == null) {
                        Toast.makeText(this, "Please select a type first!", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    else if (instrumentList[currentIndex].borrowedMonths !=0){
                        Toast.makeText(this, "Already borrowed!", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    else {
                        instrumentList[currentIndex].attribute = selectedType
                        instrumentList[currentIndex].rating = itemRating.rating
                        val intent = Intent(this, DetailsActivity::class.java)
                        intent.putExtra("selectedItem", instrumentList[currentIndex])
                        intent.putExtra("credit", credit)
                        detailLauncher.launch(intent)
                    }
                }
            }


            //Show instrument
            private fun updateInstrument() {
                if (instrumentList[currentIndex].borrowedMonths != 0){
                    btnBorrow.text = "Borrowed"
                    btnBorrow.setBackgroundColor(getColor(R.color.grey))
                    itemRadio.visibility = RadioGroup.GONE
                    itemBorrowed.visibility = TextView.VISIBLE
                    itemBorrowed.text = "Borrowed for ${instrumentList[currentIndex].borrowedMonths} months"
                }
                else{
                    btnBorrow.text = "Borrow"
                    btnBorrow.setBackgroundColor(getColor(R.color.teal))
                    itemRadio.visibility = RadioGroup.VISIBLE
                    itemBorrowed.visibility = TextView.GONE
                }
                creditText.text = "Credit: $$credit"
                var instrument = instrumentList[currentIndex]
                itemImg.setImageResource(instrument.imageRes)
                itemName.text = instrument.name
                itemRating.rating = instrument.rating
                itemPrice.text = "Price: $${instrument.price} per month"
                itemRadio.clearCheck()

            }
        }