package com.example.saveapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
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
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NoteAdaptor.OnClickListener {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdaptor: NoteAdaptor
    private lateinit var addButton: FloatingActionButton
    private lateinit var menuToolB: Toolbar

    private lateinit var getResult: ActivityResultLauncher<Intent>

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
        notesAdaptor = NoteAdaptor(this)
        recyclerView.adapter = notesAdaptor
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[NoteViewModel::class.java]

        noteViewModel.allNotes.observe(this) {
            notesAdaptor.submitList(it)
        }

        getResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Constants.ADD_REQUEST_CODE) {
                    val saveTitle = it.data?.getStringExtra(Constants.EXTRA_TITLE)
                    val saveContent = it.data?.getStringExtra(Constants.EXTRA_CONTENT)
                    val savePriority = it.data?.getIntExtra(Constants.EXTRA_PRIORITY, 1)
                    val saveNote = Note(saveTitle!!, saveContent!!, savePriority!!)
                    noteViewModel.addNote(saveNote)
                }
                else if (it.resultCode == Constants.EDIT_REQUEST_CODE) {
                    val editNoteId = it.data?.getIntExtra(Constants.EXTRA_ID, -1)
                    val editTitle = it.data?.getStringExtra(Constants.EXTRA_TITLE)
                    val editContent = it.data?.getStringExtra(Constants.EXTRA_CONTENT)
                    val editPriority = it.data?.getIntExtra(Constants.EXTRA_PRIORITY, 1)

                    val editNote = Note(editTitle!!, editContent!!, editPriority!!)
                    editNote.noteTableID = editNoteId!!
                    noteViewModel.updateNote(editNote)
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
                val noteRemoved = notesAdaptor.getNoteAt(viewHolder.bindingAdapterPosition)
                noteViewModel.deleteNote(notesAdaptor.getNoteAt(viewHolder.bindingAdapterPosition))

                Snackbar.make(this@MainActivity,recyclerView,"Deleted Note",Snackbar.LENGTH_LONG).setAction("UNDO"){
                    noteViewModel.addNote(noteRemoved)
                }.show()
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

    override fun onItemClick(note: Note) {
        val noteTitle = note.title
        val noteContent = note.content
        val notePriority = note.priority
        val noteId = note.noteTableID

        val intent = Intent(this, AddUpdateActivity::class.java)
        intent.putExtra(Constants.EXTRA_TITLE, noteTitle)
        intent.putExtra(Constants.EXTRA_CONTENT, noteContent)
        intent.putExtra(Constants.EXTRA_PRIORITY, notePriority)
        intent.putExtra(Constants.EXTRA_ID, noteId)
        getResult.launch(intent)
    }
}