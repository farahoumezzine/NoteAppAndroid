package com.example.projetmini

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NotesAdapter.DeleteNoteListener {
    private lateinit var editTextNote: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var notes: ArrayList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter
    private lateinit var saveBtn: Button
    private lateinit var constraintLayout: ConstraintLayout

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            initializeViews()
            setupRecyclerView()
            loadNotes()
            setupSaveButton()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing app", Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeViews() {
        try {
            constraintLayout = findViewById(R.id.mainLayout)
            editTextNote = findViewById(R.id.editTextNote)
            recyclerView = findViewById(R.id.notesRecycler)
            saveBtn = findViewById(R.id.saveBtn)
            sharedPreferences = getSharedPreferences("notes", MODE_PRIVATE)
            notes = ArrayList()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views: ${e.message}", e)
            throw e
        }
    }

    private fun setupRecyclerView() {
        try {
            adapter = NotesAdapter(notes)
            adapter.setDeleteNoteListener(this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up RecyclerView: ${e.message}", e)
            throw e
        }
    }

    private fun setupSaveButton() {
        try {
            saveBtn.setOnClickListener {
                try {
                    val noteText = editTextNote.text.toString().trim()
                    if (noteText.isNotEmpty()) {
                        saveNote(noteText)
                        showSuccessMessage()
                    } else {
                        Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error in save button click: ${e.message}", e)
                    Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up save button: ${e.message}", e)
            throw e
        }
    }

    private fun showSuccessMessage() {
        try {
            Snackbar.make(constraintLayout, "Note saved successfully!", Snackbar.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error showing success message: ${e.message}", e)
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveNote(note: String) {
        try {
            sharedPreferences.edit().apply {
                putString(note, note)
                apply()
            }

            notes.add(note)
            adapter.notifyDataSetChanged()
            editTextNote.text.clear()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving note: ${e.message}", e)
            throw e
        }
    }

    private fun loadNotes() {
        try {
            notes.clear()
            sharedPreferences.all.forEach { (_, value) ->
                value?.toString()?.let { notes.add(it) }
            }
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading notes: ${e.message}", e)
            Toast.makeText(this, "Error loading notes", Toast.LENGTH_LONG).show()
        }
    }

    override fun deleteNote(note: String?) {
        try {
            note?.let { nonNullNote ->
                sharedPreferences.edit().apply {
                    remove(nonNullNote)
                    apply()
                }
                loadNotes()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting note: ${e.message}", e)
            Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show()
        }
    }
}