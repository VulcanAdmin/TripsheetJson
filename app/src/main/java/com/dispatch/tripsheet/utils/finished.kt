package com.dispatch.tripsheet.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dispatch.tripsheet.R

class finished : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)


//        try {
//            sleep(1_000);
//            finish();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            setContentView(R.layout.scary_screen);
//        }
    }
}