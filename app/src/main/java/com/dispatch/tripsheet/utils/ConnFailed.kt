package com.dispatch.tripsheet.utils

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dispatch.tripsheet.Main.MainActivity
import com.dispatch.tripsheet.R


class ConnFailed : AppCompatActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conn_failed)



         val btnReturn = findViewById<View>(R.id.btnReturn) as Button

         btnReturn.setOnClickListener {
             startActivity(Intent(this, MainActivity::class.java)) }

           }}


