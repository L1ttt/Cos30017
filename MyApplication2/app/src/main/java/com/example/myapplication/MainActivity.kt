package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.room.vo.DataClass

class MainActivity : AppCompatActivity() {
    lateinit var student: RecyclerView
    var studentList: Array<String> = arrayOf("A", "B", "C", "D", "E")
    data class DataClass(val name: String)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.recycle)
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
            data.add(DataClass(studentList[i].toString()))
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
                holder.name.text = currentItem.name

        }
            override fun getItemCount(): Int {
                return Student.size
            }
            class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val name: TextView = itemView.findViewById(R.id.reView)
            }




    }
}