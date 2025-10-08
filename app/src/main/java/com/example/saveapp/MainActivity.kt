package com.example.saveapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    @Suppress("UNCHECKED_CAST")
    private val factory: ViewModelProvider.Factory = object :ViewModelProvider.Factory {
        override fun <T:ViewModel> create(modelClass: Class<T>):T{
            return NoteViewModel(application) as T
        }
    }

    private val noteViewModel: NoteViewModel by viewModels { factory }

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdaptor: NoteAdaptor
    private lateinit var addButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addButton = findViewById(R.id.add_note_button)
        recyclerView = findViewById(R.id.recycler_view)
        notesAdaptor = NoteAdaptor()
        recyclerView.adapter = notesAdaptor
        recyclerView.layoutManager = LinearLayoutManager(this)


        noteViewModel.allNotes.observe(this) {
            notesAdaptor.setNotes(it)
        }

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Constants.REQUEST_CODE) {
                val saveTitle = it.data?.getStringExtra(Constants.EXTRA_TITLE)
                val saveContent = it.data?.getStringExtra(Constants.EXTRA_DESCRIPTION)
                val savePriority = it.data?.getIntExtra(Constants.EXTRA_PRIORITY, 1)

                val  saveNote = Note(saveTitle!!, saveContent!!, savePriority!!)
                noteViewModel.addNote(saveNote)
            }
        }

        addButton.setOnClickListener {
            val intent = Intent(this, AddUpdateActivity::class.java)
            startActivity(intent)
        }
    }
}