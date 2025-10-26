package com.example.week8
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btn: Button
    lateinit var btn2:Button
    lateinit var txtInfor: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btn = findViewById(R.id.button)
        btn2 = findViewById(R.id.button2)
        txtInfor = findViewById(R.id.textView)
        btn.setOnClickListener(this)
        btn2.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button -> {
                //var file = openFileOutput("test.txt", Context.MODE_APPEND)
               // var message = "Hello World "
               // file.write(message.toByteArray())
                //file.close()
                val file = File(Environment.getExternalStorageDirectory(), "/Document/test.txt")
                file.createNewFile()
                file.writeText("Hello External Storage")
            }
            R.id.button2 -> {
                //var file = openFileInput("test.txt")
                val file_b =File(Environment.getExternalStorageDirectory(), "/Document/test.txt").exists()
                if(file_b) {
                    var file = openFileInput("/Document/test.txt")
                    val data = ByteArray(1024)
                    file.read(data)
                    txtInfor.text = data.toString(Charsets.UTF_8)
                    file.close()
                }
                else{
                    Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}