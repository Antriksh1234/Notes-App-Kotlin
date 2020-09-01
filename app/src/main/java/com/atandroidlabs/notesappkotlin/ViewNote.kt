package com.atandroidlabs.notesappkotlin

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView

class ViewNote : AppCompatActivity() {

    lateinit var view_title_TextView: TextView
    lateinit var view_content_TextView: TextView

    var id: Int = 0

    override fun onBackPressed() {
        finish()
        val intent: Intent = Intent(this@ViewNote, NotesActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        val menuInflater: MenuInflater = getMenuInflater()

        menuInflater.inflate(R.menu.menu_edit, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       super.onOptionsItemSelected(item)
        finish()
        val intent: Intent = Intent(this@ViewNote, AddNotesActivity::class.java)
        intent.putExtra("action", "edit")
        intent.putExtra("id", id)
        startActivity(intent)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)

        view_title_TextView = findViewById(R.id.view_title_TextView)
        view_content_TextView = findViewById(R.id.view_content_TextView)

        id = intent.getIntExtra("id",1)

        val database = openOrCreateDatabase("notes", MODE_PRIVATE, null)
        val c: Cursor = database.rawQuery("SELECT * FROM notes_list WHERE id = $id",null)

        val titleIndex = c.getColumnIndex("title")
        val contentIndex = c.getColumnIndex("content")

        c.moveToFirst()

        while (!c.isAfterLast) {
            val title = c.getString(titleIndex)
            val content = c.getString(contentIndex)

            view_title_TextView.setText(title)
            view_content_TextView.setText(content)

            c.moveToNext()
        }

        c.close()
    }
}