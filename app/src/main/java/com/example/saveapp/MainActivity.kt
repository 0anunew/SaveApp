package com.example.saveapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdaptor: NoteAdaptor
    private lateinit var addButton: FloatingActionButton
    private lateinit var menuToolB : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        menuToolB = findViewById(R.id.my_toolbar)
        setSupportActionBar(menuToolB)

        addButton = findViewById(R.id.add_note_button)
        recyclerView = findViewById(R.id.recycler_view)
        notesAdaptor = NoteAdaptor()
        recyclerView.adapter = notesAdaptor
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[NoteViewModel::class.java]

        noteViewModel.allNotes.observe(this) {
            notesAdaptor.setNotes(it)
        }

        val getResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Constants.REQUEST_CODE) {
                    val saveTitle = it.data?.getStringExtra(Constants.EXTRA_TITLE)
                    val saveContent = it.data?.getStringExtra(Constants.EXTRA_DESCRIPTION)
                    val savePriority = it.data?.getIntExtra(Constants.EXTRA_PRIORITY, 1)

                    val saveNote = Note(saveTitle!!, saveContent!!, savePriority!!)
                    noteViewModel.addNote(saveNote)
                }
            }

        addButton.setOnClickListener {
            val intent = Intent(this, AddUpdateActivity::class.java)
            getResult.launch(intent)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.deleteNote(notesAdaptor.getNoteAt(viewHolder.bindingAdapterPosition))
            }
        }).attachToRecyclerView(this.recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_menu -> {
                noteViewModel.deleteAllNotes()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}