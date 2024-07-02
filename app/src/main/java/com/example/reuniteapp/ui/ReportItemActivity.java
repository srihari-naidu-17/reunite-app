package com.example.reuniteapp.ui.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reuniteapp.R

class ReportItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_item)

        val itemType = intent.getStringExtra("itemType")
        // Handle the itemType to determine if it's a lost or found item
    }
}
