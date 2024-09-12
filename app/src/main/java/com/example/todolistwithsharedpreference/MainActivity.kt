package com.example.todolistwithsharedpreference

import TaskAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistwithsharedpreference.Data.Task

class MainActivity : AppCompatActivity() {
    private lateinit var tasklist: MutableList<Task>
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskadapter: TaskAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTaskEditTask: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)

        recyclerView = findViewById(R.id.Rv)
        editTaskEditTask = findViewById(R.id.editText)
        tasklist = retrieveTask()



        val saveButton: Button = findViewById(R.id.btn)

        saveButton.setOnClickListener {
            val taskText = editTaskEditTask.text.toString()
            if(taskText.isNotEmpty()){
                val task =Task(taskText,false)
                tasklist.add(task)
                saveTask(tasklist)
                taskadapter.notifyItemInserted(tasklist.size-1)
                editTaskEditTask.text.clear()
            }
            else{
                Toast.makeText(this,"Task tittle can't be empty",Toast.LENGTH_SHORT).show()
            }
        }
        taskadapter= TaskAdapter(tasklist,object : TaskAdapter.TaskClickLister{
            override fun onEditClick(position: Int) {
                editTaskEditTask.setText(tasklist[position].title)
                tasklist.removeAt(position)
                taskadapter.notifyDataSetChanged()
            }

            override fun onDeleteClick(position: Int) {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Delete Task")
                alertDialog.setMessage("Are you sure you want to delete this task?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    deleteTask(position)
                }
                alertDialog.setNegativeButton("No") { _, _ ->  }
                alertDialog.show()
            }

        })
        recyclerView.adapter= taskadapter
        recyclerView.layoutManager= LinearLayoutManager(this)
    }

    private fun saveTask(tasklist: MutableList<Task>) {
        val editor = sharedPreferences.edit()
        val taskset = HashSet<String>()

        tasklist.forEach{ taskset.add(it.title)}
        editor.putStringSet("tasks", taskset)
        editor.apply()
    }

    private fun deleteTask(position: Int) {
        tasklist.removeAt(position)
        taskadapter.notifyItemRemoved(position)
        saveTask(tasklist)
    }

    private fun retrieveTask(): MutableList<Task> {
        val tasks = sharedPreferences.getStringSet("tasks", HashSet())?:HashSet()
        return tasks.map{Task(it, false)}.toMutableList()
    }

}