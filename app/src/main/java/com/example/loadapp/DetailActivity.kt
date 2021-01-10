package com.example.loadapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    private var fileName = ""
    private var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        ok_button.setOnClickListener {
            returnToMainActivity()
        }

        fileName = intent.getStringExtra("fileName").toString()
        status = intent.getStringExtra("status").toString()
        file_name.text = fileName
        status_text.text = status
    }

    private fun returnToMainActivity() {
        val  mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
    }
}
