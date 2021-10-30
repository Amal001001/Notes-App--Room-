package com.example.notesapproom

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.notesapproom.Database.NoteDatabase
import com.example.notesapproom.Database.NoteRepository
import com.example.notesapproom.Database.Notes
import com.example.notesapproom.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private val noteDao by lazy { NoteDatabase.getDatabase(this).noteDao() }
    private val repository by lazy { NoteRepository(noteDao) }

    private lateinit var rv: RecyclerView
    lateinit var adapter: Adapter
    lateinit var items: ArrayList<Notes>

    lateinit var et:EditText
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        items = arrayListOf()
        rv = findViewById(R.id.rv)
        adapter = Adapter(this, items)

        et = findViewById(R.id.et)

        button = findViewById(R.id.button)
        button.setOnClickListener {
            val newNote = et.text.toString()
            if(newNote != "") {
                insert(newNote)
                et.text.clear()
                updateRV()
                Toast.makeText(this, "Note added", Toast.LENGTH_LONG).show()
            }
        }

        updateRV()
        readFromDB()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun readFromDB() {
        CoroutineScope(IO).launch {
            items.clear()
            items.addAll(
                noteDao.getAllNotes()
            )
            withContext(Main) {
                rv.adapter!!.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRV(){
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter!!.notifyDataSetChanged()
    }


    private fun insert(noteText: String){
        CoroutineScope(IO).launch {
            repository.insert(Notes(0, noteText))
            readFromDB()
        }
    }

    private fun update(noteID: Int, noteText: String){
        CoroutineScope(IO).launch {
            repository.update(Notes(noteID,noteText))
            readFromDB()
        }
    }

    fun delete(noteID: Int){
        CoroutineScope(IO).launch {
            repository.delete(Notes(noteID,""))
            readFromDB()
        }
    }

    fun raiseDialog(id: Int,note:String){

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Update Recipe")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val updatedNote = EditText(this)
        updatedNote.setText(note)
        layout.addView(updatedNote)

        layout.setPadding(50, 40, 50, 10)
        alert.setView(layout)

        alert.setPositiveButton("Update") { _, _ ->
            val updatedNote = updatedNote.text.toString()
            update(id, updatedNote)
            Toast.makeText(this, "Updated Sucessfully", Toast.LENGTH_LONG).show()
        }

        alert.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        alert.setCancelable(false)
        alert.show()
    }

        @SuppressLint("SetTextI18n")
        fun deleteDialog(item: Notes) {
            val dialogBuilder = AlertDialog.Builder(this)
            val confirmDelete = TextView(this)
            confirmDelete.text = "  Are you sure you want to delete this note?"
            dialogBuilder.setCancelable(false).setPositiveButton("Yes",
                    DialogInterface.OnClickListener { _, _ -> delete(item.id) })
                   .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
            val alert = dialogBuilder.create()
            alert.setTitle("Delete Note")
            alert.setView(confirmDelete)
            alert.show()

        }

}