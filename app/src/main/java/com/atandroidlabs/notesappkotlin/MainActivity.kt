package com.atandroidlabs.notesappkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app_name: TextView = findViewById(R.id.textView)
        val subtitle: TextView = findViewById(R.id.textView2)

        app_name.animate().alpha(1F).duration = 1000
        subtitle.animate().alpha(1F).duration = 1000

        //var timer: CountDownTimer = CountDownTimer(4000,1000)

        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //Do nothing just wait
            }

            override fun onFinish() {
                //var intent: Intent = Intent(this@MainActivity, NotesActivity::class)
                finish()
                val intent = Intent(applicationContext, NotesActivity::class.java)
                startActivity(intent)
            }

        }.start()

    }
}