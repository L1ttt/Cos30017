package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class MainActivity : AppCompatActivity() {
    lateinit var student: RecyclerView
    var studentList: Array<String> = arrayOf("A", "B", "C", "D", "E")
    var imageList: Array<Int> = arrayOf(R.drawable.download1, R.drawable.download, R.drawable.download1, R.drawable.download, R.drawable.download1)
    data class DataClass(val name: String, val image: Int)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        student = findViewById(R.id.student)
        student.layoutManager = LinearLayoutManager(this)
        student.setHasFixedSize(true)
        val data = ArrayList<DataClass>()
        for (i in 0 until studentList.size)  {

            data.add(DataClass( studentList[i].toString(), imageList[i],))
        }
        val adapter = RecyclerAdapter(data)
        student.setAdapter(adapter)
    }


    class RecyclerAdapter(private val Student: List<DataClass>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.templet_re, parent, false)
            return ViewHolderClass(itemView)

        }
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            val currentItem = Student[position]
            holder.name.setText(currentItem.name)
            holder.image.setImageResource(currentItem.image)
        }
        override fun getItemCount(): Int {
            return Student.size
        }
        class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.reView)
            val image: ImageView = itemView.findViewById(R.id.image)
        }




    }
}


