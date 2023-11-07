package com.example.asn1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Map : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        saveButton.setOnClickListener(){
            saveMap();
        }

        cancelButton.setOnClickListener(){
            cancelMap();
        }
    }

    private fun saveMap(){
        finish()
    }

    private fun cancelMap(){
        finish()
    }
}