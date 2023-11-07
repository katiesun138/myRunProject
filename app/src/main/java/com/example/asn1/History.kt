package com.example.asn1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat


class History : Fragment() {
    private lateinit var myListView:ListView

    private lateinit var arrayList: ArrayList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private lateinit var database: HistoryDatabase
    private lateinit var databaseDao: HistoryDataDao
    private lateinit var repository: HistoryRepository
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyViewModelFactory: HistoryViewModelFactory

    private lateinit var historyList: List<HistoryData>

    override fun onResume() {
        super.onResume()
        displayAllHistory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        myListView = view.findViewById(R.id.historyListView)

        arrayList = ArrayList()
        arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayList)
        myListView.adapter = arrayAdapter

        myListView.setOnItemClickListener{ _, _, position, _ ->
            val selectedHistoryData = historyList[position]

            val intent = Intent(requireContext(), DetailedHistory::class.java)

            intent.putExtra("historyEntryId", selectedHistoryData.id)

            Log.d("selected id", selectedHistoryData.id.toString())

            startActivity(intent)

            Toast.makeText(requireContext(), "Detailed View Loading...", Toast.LENGTH_SHORT).show()

        }

        database = HistoryDatabase.getInstance(requireActivity())
        databaseDao = database.historyDataDao
        repository = HistoryRepository(databaseDao)
        historyViewModelFactory = HistoryViewModelFactory(repository)
        historyViewModel = ViewModelProvider(requireActivity(), historyViewModelFactory).get(HistoryViewModel::class.java)


        displayAllHistory()

        return view;
    }

    private fun displayAllHistory(){
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


        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val unit = sharedPreferences.getString("unitPreference", "km") ?: ""




        historyViewModel.allHistoryLiveData.observe(requireActivity()){
                historyList ->
            this.historyList = historyList

            arrayList.clear() // Clear the existing data
            arrayList.addAll(historyList.map { historyData ->

                val inputType = inputTypeMap[historyData.inputType];
                val activityType = activityTypeMap[historyData.activityType];

                val sdf = SimpleDateFormat("HH:mm:ss MMM d yyyy")
                val dateTimeString = sdf.format(historyData.dateTime.time)
                var distance = 0.0;

                if (unit == "miles"){
                    distance = historyData.distance /1.609344
                    distance = String.format("%.2f", distance).toDouble()
                }
                else{
                    distance = historyData.distance
                }

                "$inputType : $activityType, $dateTimeString \n $distance $unit, ${historyData.duration} secs"
            })
            arrayAdapter.notifyDataSetChanged()
            println("ks:list size: ${historyList.size}")
        }
    }


}