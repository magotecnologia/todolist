package com.magotecnologia.todolist

import android.R
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.magotecnologia.todolist.databinding.ActivityMainBinding
import java.io.PrintStream
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val taskList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadTasks()
        binding.buttonCreate.setOnClickListener {
            writeTask()
        }
        binding.listTodo.adapter = ArrayAdapter(this, R.layout.simple_list_item_1, taskList)
        binding.listTodo.setOnItemLongClickListener { _, _, position, _ ->
            deleteTask(position)
            true
        }
    }

    private fun deleteTask(position: Int) {
        taskList.removeAt(position)
        (binding.listTodo.adapter as BaseAdapter).notifyDataSetChanged()
        saveTask(taskList)
    }

    private fun saveTask(taskList: MutableList<String>) {
        val output = PrintStream(openFileOutput(FILE_NAME, Context.MODE_PRIVATE))
        taskList.forEach { output.println(it) }
        output.flush()
        output.close()
    }


    private fun loadTasks() {
        try {
            val scan = Scanner(openFileInput(FILE_NAME))
            while (scan.hasNextLine()) {
                taskList.add(scan.nextLine())
            }
            scan.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun writeTask() {
        val output = PrintStream(openFileOutput(FILE_NAME, Context.MODE_APPEND))
        output.println(binding.editNewTask.text.toString())
        output.close()
        taskList.add(binding.editNewTask.text.toString())
        (binding.listTodo.adapter as BaseAdapter).notifyDataSetChanged()
    }

    companion object {
        const val FILE_NAME = "Task.txt"
    }
}