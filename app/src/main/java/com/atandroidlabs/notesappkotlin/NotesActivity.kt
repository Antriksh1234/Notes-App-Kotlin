package com.atandroidlabs.notesappkotlin

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class NotesActivity : AppCompatActivity() {

    var notesTitleList: ArrayList<String>? = null
    var notesList: ArrayList<Note>? = null

    var adapter: MyAdapter?= null
    var notes_ListView: ListView? = null

    class MyAdapter(context: Context?, resource: Int, objects: ArrayList<String>?) :
        ArrayAdapter<Any?>(context!!, resource, objects!! as List<Any?>) {

        var notesTitlesList: ArrayList<String>? = objects
        override fun getCount(): Int {
            return super.getCount()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val v: View
            val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.note_item, null)
            val textView = v.findViewById<View>(R.id.note_name) as TextView
            textView.setText(notesTitlesList!!.get(position))
            return v
        }
    }

    fun openAddNotesActivity(view: View) {
        val intent: Intent = Intent(this@NotesActivity, AddNotesActivity::class.java)
        intent.putExtra("action","add")
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        notes_ListView = findViewById(R.id.notes_listView)

        notesTitleList = ArrayList<String>()
        notesList = ArrayList<Note>()

        val database: SQLiteDatabase = this.openOrCreateDatabase("notes", MODE_PRIVATE, null)
        database.execSQL("CREATE TABLE IF NOT EXISTS notes_list (id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, content VARCHAR)")
        val cursor: Cursor = database.rawQuery("SELECT * FROM notes_list", null)

        val titleIndex: Int = cursor.getColumnIndex("title")
        val contentIndex: Int = cursor.getColumnIndex("content")
        val idIndex =  cursor.getColumnIndex("id")

        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            val noteTitle: String = cursor.getString(titleIndex)
            val noteContent: String = cursor.getString(contentIndex)
            val id: Int = cursor.getInt(idIndex)

            val note: Note = Note(id, noteTitle, noteContent)

            notesList!!.add(note)
            notesTitleList!!.add(noteTitle)
            cursor.moveToNext()
        }

        cursor.close()

        adapter = MyAdapter(this@NotesActivity, R.layout.note_item, notesTitleList)

        notes_ListView!!.adapter = adapter

        notes_ListView!!.setOnItemClickListener(OnItemClickListener { arg0, arg1, position, arg3 ->
            finish()
            val intent: Intent = Intent(applicationContext, ViewNote::class.java)
            intent.putExtra("id", notesList!!.get(position).id)
            startActivity(intent)
        })

        notes_ListView!!.setOnItemLongClickListener(OnItemLongClickListener { av, v, pos, id ->
            val note_id = notesList!!.get(pos).id
            database.execSQL("DELETE FROM notes_list WHERE id = $note_id")
            Toast.makeText(
                this,
                notesList!!.get(pos).title.toString() + " deleted",
                Toast.LENGTH_SHORT
            ).show()
            notesTitleList!!.remove(notesTitleList!!.get(pos))
            notesList!!.remove(notesList!!.get(pos))
            adapter!!.notifyDataSetChanged()

            return@OnItemLongClickListener true
        })
    }
}