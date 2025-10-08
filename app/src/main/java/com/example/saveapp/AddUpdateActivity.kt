package com.example.saveapp
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class AddUpdateActivity : AppCompatActivity() {

    private lateinit var editTitleText: EditText
    private lateinit var editContentText: EditText
    private lateinit var numberPickerPriority: NumberPicker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_update)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        numberPickerPriority = findViewById(R.id.number_picker_priority)

        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 10

        editTitleText = findViewById(R.id.edit_text_title)
        editContentText = findViewById(R.id.edit_text_content)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        title = "Add Note"
    }

    fun saveNote(){
        val title = editTitleText.text.toString()
        val content = editContentText.text.toString()
        val priority = numberPickerPriority.value

        if (title.trim().isEmpty() || content.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and content", Toast.LENGTH_SHORT).show()
            return
        }

        setResult(Constants.REQUEST_CODE, Intent().apply {
            putExtra(Constants.EXTRA_TITLE, title)
            putExtra(Constants.EXTRA_DESCRIPTION, content)
            putExtra(Constants.EXTRA_PRIORITY, priority)
        })
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu_item -> {
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}