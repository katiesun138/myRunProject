package com.example.asn1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat

class DetailedHistory : AppCompatActivity() {

    private lateinit var database: HistoryDatabase
    private lateinit var databaseDao: HistoryDataDao
    private lateinit var repository: HistoryRepository
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyViewModelFactory: HistoryViewModelFactory

    private lateinit var singleEntry: HistoryData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_history)

        val inputTypeFromStart = intent.getLongExtra("historyEntryId", -1)
        Log.d("in detailedhistory id", inputTypeFromStart.toString())
        val id: Long = inputTypeFromStart.toLong();

        database = HistoryDatabase.getInstance(this)
        databaseDao = database.historyDataDao
        repository = HistoryRepository(databaseDao)
        historyViewModelFactory = HistoryViewModelFactory(repository)
        historyViewModel =
            ViewModelProvider(this, historyViewModelFactory).get(HistoryViewModel::class.java)

        val deleteButton = findViewById<Button>(R.id.headerButton)

        deleteButton.setOnClickListener() {
            historyViewModel.deleteById(id);
            Toast.makeText(this, "Delete item", Toast.LENGTH_SHORT).show()
            finish()

        }

        val inputTypeMap = mapOf(
            0 to "Manual Entry" ,
            1 to "GPS",
            2 to "Automatic"
        )

        val activityTypeMap = mapOf(
            0 to "Running",
            1 to "Walking",
            2 to "Standing" ,
            3 to "Cycling",
            4 to "Hiking",
            5 to "Downhill Skiing",
            6 to "Cross-Country Skiing",
            7 to "Snowboarding",
            8 to "Skating",
            9 to "Swimming",
            10 to "Mountain Biking",
            11 to "Wheelchair",
            12 to "Elliptical",
            13 to "Other"
        )


        historyViewModel.allHistoryLiveData.observe(this) { historyList ->

            historyList.map { historyData ->

                if (historyData.id == id) {
                    singleEntry = historyData
                    Log.d("selected Activity", singleEntry.activityType.toString())
                    val sdf = SimpleDateFormat("HH:mm:ss MMM d yyyy")
                    val dateTimeString = sdf.format(historyData.dateTime.time)

                    val inputType = inputTypeMap[singleEntry.inputType]
                    val activityType = activityTypeMap[singleEntry.activityType]

                    val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val unit = sharedPreferences.getString("unitPreference", "km") ?: ""

                    var distance = 0.0;

                    if (unit == "miles"){
                        distance = singleEntry.distance /1.609344
                        distance = String.format("%.2f", distance).toDouble()

                    }
                    else{
                        distance = singleEntry.distance
                    }

                    val items = arrayOf(
                        "Input Type \n $inputType",
                        "Activity Type \n $activityType",
                        "Date and Time \n $dateTimeString",
                        "Duration \n ${singleEntry.duration} secs",
                        "Distance \n $distance $unit",
                        "Calories \n ${singleEntry.calorie} cals",
                        "Heart Rate \n ${singleEntry.heartRate} bpm"
                    )

                    val customAdapter = CustomAdapter(this, items)
                    val listView = findViewById<ListView>(R.id.inputsListView)
                    listView.adapter = customAdapter


                }

            }

        }
    }
}