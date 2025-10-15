package com.example.saveapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.coroutines.Continuation


class AddUpdateActivity : AppCompatActivity() {

    private lateinit var editTitleText: EditText
    private lateinit var editContentText: EditText
    private lateinit var numberPickerPriority: NumberPicker

    private lateinit var saveButton: AppCompatButton
    private lateinit var closeButton: AppCompatButton


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

        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            saveNote()
        }
        closeButton = findViewById(R.id.close_button)
        closeButton.setOnClickListener {
            finish()
        }

        if (intent.hasExtra(Constants.EXTRA_TITLE)) {
            editTitleText.setText(intent.getStringExtra(Constants.EXTRA_TITLE))
            editContentText.setText(intent.getStringExtra(Constants.EXTRA_CONTENT))
            numberPickerPriority.value = intent.getIntExtra(Constants.EXTRA_PRIORITY, 1)
            saveButton.text = getString(R.string.update)
        } else {
            editTitleText.setHint(R.string.add_title)
            editContentText.setHint(R.string.add_content)
            numberPickerPriority.value = 1
            saveButton.text = getString(R.string.save)
        }


    }

    fun saveNote() {
        val title = editTitleText.text.toString()
        val content = editContentText.text.toString()
        val priority = numberPickerPriority.value

        if (title.trim().isEmpty() || content.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and content", Toast.LENGTH_SHORT).show()
            return
        }

        val extraID = intent.getIntExtra(Constants.EXTRA_ID, -1)

        if (extraID != -1) {
            setResult(Constants.EDIT_REQUEST_CODE, Intent().apply {
                putExtra(Constants.EXTRA_TITLE, title)
                putExtra(Constants.EXTRA_CONTENT, content)
                putExtra(Constants.EXTRA_PRIORITY, priority)
                putExtra(Constants.EXTRA_ID, extraID)
            })
        } else {
            setResult(Constants.ADD_REQUEST_CODE, Intent().apply {
                putExtra(Constants.EXTRA_TITLE, title)
                putExtra(Constants.EXTRA_CONTENT, content)
                putExtra(Constants.EXTRA_PRIORITY, priority)
            })
        }
        finish()
    }
}