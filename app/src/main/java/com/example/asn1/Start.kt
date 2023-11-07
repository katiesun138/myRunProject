package com.example.asn1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Start.newInstance] factory method to
 * create an instance of this fragment.
 */
class Start : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val input = resources.getStringArray(R.array.inputType)
        val activity = resources.getStringArray(R.array.activityType)



        val view = inflater.inflate(R.layout.fragment_start, container, false)

        val spinnerInput = view.findViewById<Spinner>(R.id.spinnerInput)
        val spinnerActivity = view.findViewById<Spinner>(R.id.spinnerActivity)


        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            input
        )

        val adapter2 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            activity
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerInput.adapter = adapter
        spinnerActivity.adapter = adapter2


        val startButton = view.findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener() {

            val intentMap = Intent(requireContext(), Map::class.java)
            val intent = Intent(requireContext(), StartInput::class.java)

            val inputType = spinnerInput.selectedItem.toString()
            if (inputType != "Manual Entry") {
                startActivity(intentMap)
            }
            else {

                val inputTypeMap = mapOf(
                    "Manual Entry" to 0,
                    "GPS" to 1,
                    "Automatic" to 2
                )

                val activityTypeMap = mapOf(
                    "Running" to 0,
                    "Walking" to 1,
                    "Standing" to 2,
                    "Cycling" to 3,
                    "Hiking" to 4,
                    "Downhill Skiing" to 5,
                    "Cross-Country Skiing" to 6,
                    "Snowboarding" to 7,
                    "Skating" to 8,
                    "Swimming" to 9,
                    "Mountain Biking" to 10,
                    "Wheelchair" to 11,
                    "Elliptical" to 12,
                    "Other" to 13
                )

                val selectedItemInputType = spinnerInput.selectedItem.toString()
                val numericInputType = inputTypeMap[selectedItemInputType]
                val selectedActivity = spinnerActivity.selectedItem.toString()
                val numericActivity = activityTypeMap[selectedActivity]
                intent.putExtra("inputType", numericInputType);
                intent.putExtra("activityType", numericActivity);
                startActivity(intent)
                Toast.makeText(requireContext(), "Button clicked!", Toast.LENGTH_SHORT).show()
            }
        }




        return view
    }


}