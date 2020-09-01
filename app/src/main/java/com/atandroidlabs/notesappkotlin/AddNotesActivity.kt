package com.atandroidlabs.notesappkotlin

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class AddNotesActivity : AppCompatActivity() {

    var titleText: EditText? = null
    var contentText: EditText? = null
    var id = -1
    lateinit var action: String
    override fun onBackPressed() {
        goBack()
    }

    private fun goBack() {
        finish()
        val intent: Intent = Intent(this@AddNotesActivity, NotesActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        titleText = findViewById(R.id.notes_title_editText)
        contentText = findViewById(R.id.notes_content_editText)

        val button: Button = findViewById(R.id.button)
        val instructionText: TextView = findViewById(R.id.textView3)

        action = intent.getStringExtra("action") as String

        if (action == "edit") {
            button.setText("edit")
            instructionText.setText("Edit Your Note")
            id = intent.getIntExtra("id", 0)
            setTextOfTitleAndContent(id)
        }
    }

    fun addNote(view: View) {
        if (action == "edit") {
            updateNoteInDatabase()
        } else {
            insertDataInDatabase()
        }
    }

    private fun setTextOfTitleAndContent(id: Int) {
        var title: String
        var content: String

        val database: SQLiteDatabase = this.openOrCreateDatabase("notes", MODE_PRIVATE, null)
        val c: Cursor = database.rawQuery("SELECT * FROM notes_list WHERE id = $id", null)
        val titleIndex = c.getColumnIndex("title")
        val contentIndex = c.getColumnIndex("content")

        c.moveToFirst()

        while (!c.isAfterLast) {
            title = c.getString(titleIndex)
            content = c.getString(contentIndex)

            titleText!!.setText(title)
            contentText!!.setText(content)

            c.moveToNext()
        }

        c.close()
    }

    private fun insertDataInDatabase() {
        val database: SQLiteDatabase = this.openOrCreateDatabase("notes", MODE_PRIVATE, null)

        val title: String = titleText!!.text.toString()
        val content: String = contentText!!.text.toString()

        if (title.isEmpty()) {
            Toast.makeText(this@AddNotesActivity, "Enter some title", Toast.LENGTH_SHORT).show()
        } else {
            database.execSQL("INSERT INTO notes_list(title, content) VALUES ('$title','$content')")
            goBack()
        }
    }

    private fun updateNoteInDatabase() {
        val database: SQLiteDatabase = this.openOrCreateDatabase("notes", MODE_PRIVATE, null)

        val title: String = titleText!!.text.toString()
        val content: String = contentText!!.text.toString()

        if (title.isEmpty()) {
            Toast.makeText(this@AddNotesActivity, "Enter some title / content", Toast.LENGTH_SHORT).show()
        } else {
            database.execSQL(" UPDATE notes_list set title = '$title', content = '$content' WHERE id = $id")
            goBack()
        }
    }
}