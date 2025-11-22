package com.example.assignment3
import android.app.AlertDialog
import android.content.Intent
import android.widget.TextView
import android.os.Bundle
import android.text.InputType

import android.view.LayoutInflater

import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.Serializable
data class Player(var name: String, var score: Int, var color: Int,var decrease:Int,var increase:Int ) : Serializable
private var colorList =listOf(
    R.color.brown_500,
    R.color.indigo_500,
    R.color.blue_500,
    R.color.blue_700,
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
private var nameList = listOf(
    "Cow", "Dog", "Cat", "Lion", "Tiger", "Bear", "Elephant", "Monkey", "Zebra",
    "Giraffe", "Hippo", "Duck", "Chicken", "Pig", "Horse", "Sheep", "Goat",
    "Dolphin", "Donkey", "Deer", "Fox", "Wolf", "Shark", "Whale", "Eagle",
    "Owl", "Penguin", "Kangaroo", "Snake", "Frog"
)

class MainActivity : AppCompatActivity() {

    private lateinit var flexContainer: FlexboxLayout

    private lateinit var btnDeleteAll: ImageButton
    private lateinit var btnAddBlock: ImageButton

    private lateinit var tvTotalScore: TextView
    private lateinit var diceBtn: LinearLayout

    private lateinit var settingBtn: LinearLayout

    private val players = mutableListOf<Player>()
    private val cardViews = mutableListOf<Pair<Player, android.view.View>>()
    private val fileName = "score_data.json"
    private var sort = "none"
    private var header = "total"
    private var dice = "dice"

    private val autoSortHandler = android.os.Handler(android.os.Looper.getMainLooper())
    private val autoSortRunnable = Runnable {
        if (sort != "none") {
            sort()
        }
    }
    private val delayTime = 3000L

    private val configActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val action = data?.getStringExtra("ACTION")

            if (action == "DELETE_PLAYER") {
                val playerIndex = data.getIntExtra("PLAYER_INDEX", 0)
                val playerToRemove = players[playerIndex]
                players.remove(playerToRemove)
                saveToFile()
                sort()
            } else if (action == "UPDATE_PLAYER") {
                val playerIndex = data.getIntExtra("PLAYER_INDEX", 0)
                val playerName = data.getStringExtra("PLAYER_NAME")
                val playerScore = data.getIntExtra("PLAYER_SCORE", 0)
                val playerColor = data.getIntExtra("PLAYER_COLOR", colorList.random())
                val playerDecrease = data.getIntExtra("PLAYER_DECREASE", 1)
                val playerIncrease = data.getIntExtra("PLAYER_INCREASE", 1)

                val playerToUpdate = players[playerIndex]
                players.remove(playerToUpdate)
                val newPlayer = Player(playerName.toString(), playerScore, playerColor,playerDecrease,playerIncrease)
                players.add(playerIndex, newPlayer)
                saveToFile()
                sort()
            }
            else if(action == "UPDATE_SETTING"){
                sort = data.getStringExtra("sort").toString()
                header = data.getStringExtra("header").toString()
                dice = data.getStringExtra("dice").toString()
                sort()
            }
            else if (action == "NAV_SETTING"){
                openSetting()
            }
            else if (action == "NAV_DICE"){
                sort = data.getStringExtra("sort").toString()
                header = data.getStringExtra("header").toString()
                dice = data.getStringExtra("dice").toString()
                sort()
                openDice()
            }
        }
        else{
            sort()
        }
    }
    private fun openSetting(){
        val intent = Intent(this, SettingActivity::class.java)
        intent.putExtra("sort",sort)
        intent.putExtra("header",header)
        intent.putExtra("dice",dice)
        configActivityResultLauncher.launch(intent)
    }
    private fun openDice(){
        val intent = Intent(this, DiceActivity::class.java)
        intent.putExtra("dice",dice)
        intent.putExtra("players", ArrayList(players))
        configActivityResultLauncher.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flexContainer = findViewById(R.id.flexContainer)
        btnAddBlock = findViewById(R.id.btnAddBlock)
        btnDeleteAll = findViewById(R.id.btnDeleteAll)
        tvTotalScore = findViewById(R.id.tvTotalScore)
        diceBtn = findViewById(R.id.nav_dice)
        settingBtn = findViewById(R.id.nav_setting)

        diceBtn.setOnClickListener {
            openDice()
        }
        settingBtn.setOnClickListener {
            openSetting()
        }


        // Load previous data
        sort()


        btnAddBlock.setOnClickListener {
            addNewCard()
            updateCardHeights()
            resetAutoSortTimer()
        }
        tvTotalScore.setOnClickListener {
            if(header == "total"){
                header = "win"
            }
            else if (header == "win"){
                header = "lose"
            }
            else{
                header = "total"
            }
            updateTotalScore()
        }
        btnDeleteAll.setOnClickListener {
            players.clear()
            cardViews.clear()
            flexContainer.removeAllViews()
            saveToFile()
            updateTotalScore()

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        stopAutoSortTimer()
    }


    private fun addNewCard(player: Player? = null) {

        val cardView = LayoutInflater.from(this).inflate(R.layout.player_card, flexContainer, false)

        val randomColor = colorList.random()
        val newPlayer = player ?: Player(nameList.random(), 0, randomColor,1,1)
        if (player == null) {
            players.add(newPlayer)
        }
        val tvTitle = cardView.findViewById<TextView>(R.id.tvTitle)
        val tvScore = cardView.findViewById<TextView>(R.id.tvScore)
        val btnAddScore = cardView.findViewById<TextView>(R.id.btnAddScore)
        val btnMinusScore = cardView.findViewById<TextView>(R.id.btnMinusScore)
        val btnConfig = cardView.findViewById<ImageView>(R.id.btnConfig)
        val CardView = cardView.findViewById<androidx.cardview.widget.CardView>(R.id.playerCard)
        CardView.setCardBackgroundColor(ContextCompat.getColor(this, newPlayer.color))

        tvTitle.text = newPlayer.name
        tvScore.text = "${newPlayer.score}"

        var currentScore = newPlayer.score
        btnAddScore.setOnClickListener {
            currentScore += newPlayer.increase
            newPlayer.score = currentScore
            tvScore.text = "$currentScore"
            updateTotalScore()
            saveToFile()
            resetAutoSortTimer()
        }
        btnMinusScore.setOnClickListener {
            currentScore -= newPlayer.decrease
            newPlayer.score = currentScore
            tvScore.text = "$currentScore"
            updateTotalScore()
            saveToFile()
            resetAutoSortTimer()
        }
        btnConfig.setOnClickListener {
            stopAutoSortTimer()
            val playerIndex = players.indexOf(newPlayer)
            val intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra("PlayerIndex", playerIndex)
            intent.putExtra("Name", newPlayer.name)
            intent.putExtra("Score", newPlayer.score)
            intent.putExtra("Color", newPlayer.color)
            intent.putExtra("Decrease",newPlayer.decrease)
            intent.putExtra("Increase",newPlayer.increase)
            configActivityResultLauncher.launch(intent)

        }
        tvTitle.setOnClickListener {
            stopAutoSortTimer()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Set Player Name")

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.setText(tvTitle.text)
            builder.setView(input)

            builder.setPositiveButton("OK") { _, _ ->
                tvTitle.text = input.text.toString()
                newPlayer.name = input.text.toString()
                saveToFile()
                sort()
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
        tvScore.setOnClickListener {
            stopAutoSortTimer()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Set Score")

            val input = EditText(this)
            input.inputType =  InputType.TYPE_NUMBER_FLAG_SIGNED
            input.setText(tvScore.text)
            builder.setView(input)

            builder.setPositiveButton("OK") { _, _ ->
                tvScore.text = input.text.toString()
                newPlayer.score = input.text.toString().toInt()
                saveToFile()
                sort()
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }


        flexContainer.addView(cardView)
        // Add to cardViews list
        cardViews.add(Pair(newPlayer, cardView))
        updateTotalScore()
        saveToFile()

    }

    private fun updateTotalScore() {
        if (players.none()){
            tvTotalScore.text = "Score Dashboard"
            tvTotalScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.score, 0, 0, 0)
        }
        else {
            if (header == "win") {
                val max = players.maxByOrNull { it.score }
                val print = "${max?.name} (${max?.score})"
                tvTotalScore.text = print
                tvTotalScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.winner, 0, 0, 0)
            } else if (header == "lose") {
                val min = players.minByOrNull { it.score }
                val print = "${min?.name} (${min?.score})"
                tvTotalScore.text = print
                tvTotalScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lose, 0, 0, 0)
            } else if (header == "total") {
                val print = players.sumOf { it.score }
                tvTotalScore.text = print.toString()
                tvTotalScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.total, 0, 0, 0)
            }
        }



    }

    private fun saveToFile() {
        val jsonArray = JSONArray()
        players.forEach {
            val obj = JSONObject()
            obj.put("name", it.name)
            obj.put("score", it.score)
            obj.put("color", it.color)
            obj.put("decrease",it.decrease)
            obj.put("increase",it.increase)
            jsonArray.put(obj)
        }
        val file = File(filesDir, fileName)
        file.writeText(jsonArray.toString())
    }


    private fun loadFromFile() {
        val file = File(filesDir, fileName)
        if (!file.exists() || file.readText().isBlank()) return

        players.clear()
        cardViews.clear()
        flexContainer.removeAllViews()

        val content = file.readText()
        val jsonArray = JSONArray(content)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val player = Player(
                obj.getString("name"),
                obj.getInt("score"),
                obj.getInt("color"),
                obj.getInt("decrease"),
                obj.getInt("increase")
            )
            players.add(player)
            addNewCard(player)
        }
        updateTotalScore()
        updateCardHeights()
    }

    private fun updateCardHeights() {
        val count = flexContainer.childCount
        if (count == 0) return
        val minHeight = 400
        val totalHeight = 1800
        var heightPerCard = totalHeight / count
        if (heightPerCard<= minHeight){
            heightPerCard = minHeight
        }

        for (i in 0 until count) {
            val card = flexContainer.getChildAt(i)
            val params = card.layoutParams
            params.height = heightPerCard
            card.layoutParams = params
        }
    }
    private fun sort(){
        if (sort == "increase") {
            players.sortBy { it.score }
            saveToFile()
            loadFromFile()
        }
        else if(sort == "decrease"){
            players.sortByDescending { it.score }
            saveToFile()
            loadFromFile()
        }
        else{
            loadFromFile()
        }
    }
    private fun startAutoSortTimer() {
        autoSortHandler.removeCallbacks(autoSortRunnable)
        autoSortHandler.postDelayed(autoSortRunnable, delayTime)
    }


    private fun resetAutoSortTimer() {
        startAutoSortTimer()
    }


    private fun stopAutoSortTimer() {
        autoSortHandler.removeCallbacks(autoSortRunnable)
    }

}
