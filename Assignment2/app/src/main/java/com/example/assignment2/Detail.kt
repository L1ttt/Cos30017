package com.example.assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import java.util.Calendar

class DetailsActivity : AppCompatActivity() {

    private lateinit var detailItemImg: ImageView
    private lateinit var detailItemName: TextView
    private lateinit var detailItemRate: TextView
    private lateinit var detailItemAttribute: TextView
    private lateinit var detailItemType: TextView
    private lateinit var detailItemModel: TextView
    private lateinit var detailItemColor: TextView
    private lateinit var detailItemCondition: TextView

    private lateinit var detailItemPrice: TextView
    private lateinit var borrowMonthsSlider: Slider
    private lateinit var borrowMonthsText: TextView
    private lateinit var saveBtn: Button
    private lateinit var cancelBtn: Button


    private lateinit var item: Instrument

    private lateinit var creditText: TextView
    private var credit: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@DetailsActivity)
                    .setTitle("Cancel Changes?")
                    .setMessage("Are you sure you want to go back? Any changes will be lost.")
                    .setPositiveButton("Yes") { _, _ ->
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        // --- END OF NEW CODE ---
        // Link the XML views to Kotlin objects
        detailItemImg = findViewById(R.id.detailItemImg)
        detailItemName = findViewById(R.id.detailItemName)
        detailItemRate = findViewById(R.id.detailItemRate)
        detailItemAttribute = findViewById(R.id.detailItemAttribute)
        detailItemType = findViewById(R.id.detailItemType)
        detailItemModel = findViewById(R.id.detailItemModel)
        detailItemPrice = findViewById(R.id.detailItemPrice)
        detailItemColor = findViewById(R.id.detailItemColor)
        detailItemCondition = findViewById(R.id.detailItemCondition)
        creditText = findViewById(R.id.credit)
        borrowMonthsSlider = findViewById(R.id.borrowMonthsSlider)
        borrowMonthsText = findViewById(R.id.borrowMonthsText)
        saveBtn = findViewById(R.id.saveBtn)
        cancelBtn = findViewById(R.id.cancelBtn)



        // Get the selected item from the intent

        item = intent.getParcelableExtra("selectedItem")!!
        credit = intent.getIntExtra("credit", 0)


        populateDetails(item)

        borrowMonthsSlider.addOnChangeListener { _, value, _ ->
            val months = value.toInt()
            borrowMonthsText.text = "Borrow for: $months months"
            updateTotalPrice(months)
        }
        cancelBtn.setOnClickListener {
            finish()
        }
        saveBtn.setOnClickListener {
            val borrowedMonths = borrowMonthsSlider.value.toInt()

            if (borrowedMonths < 1) {
                Toast.makeText(this, "Minimum borrow period is 1 month!", Toast.LENGTH_SHORT).show()
            }
            else if (borrowedMonths* item.price > credit){
                Toast.makeText(this, "Not enough credit!", Toast.LENGTH_SHORT).show()
            }
                else {
                item.borrowedMonths = borrowedMonths
                credit = credit - borrowedMonths * item.price


                val resultIntent = Intent().apply {
                    putExtra("updatedInstrument", item)
                    putExtra("borrowMonths", borrowedMonths)
                    putExtra("updatedCredit", credit)
                }

                Toast.makeText(this, "Successfully borrowed for $borrowedMonths months", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun populateDetails(item: Instrument) {
        creditText.text = "Credit: $$credit"
        detailItemName.text = item.name
        detailItemAttribute.text = "Attribute: ${item.attribute}"
        detailItemRate.text = "Rate: ${item.rating}"
        detailItemType.text = "Type: ${ item.instrumentType }"
        detailItemModel.text = "Model: ${item.instrumentModel}"
        detailItemColor.text = "Color: ${item.instrumentColor}"
        detailItemCondition.text = "Condition: ${item.instrumentCondition}"
        detailItemImg.setImageResource(item.imageRes)
        updateTotalPrice(borrowMonthsSlider.value.toInt())
        borrowMonthsText.text = "Borrow for: ${borrowMonthsSlider.value.toInt()} months"
    }

    private fun updateTotalPrice(months: Int) {
        val totalPrice = months * item.price
        detailItemPrice.text = "Price: $${totalPrice}"
    }

}