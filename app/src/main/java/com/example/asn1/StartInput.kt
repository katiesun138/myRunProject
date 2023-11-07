package com.example.asn1

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import java.util.Calendar

class StartInput : AppCompatActivity() {

    private lateinit var database: HistoryDatabase
    private lateinit var databaseDao: HistoryDataDao
    private lateinit var repository: HistoryRepository
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyViewModelFactory: HistoryViewModelFactory


    private var selectedDate: String = ""
    private var selectedTime: String = "";
    private var selectedDuration: Double? = null
    private var selectedDistance: Double? = null
    private var selectedCalories: Double? = null
    private var selectedHeartRate: Double? = null
    private var selectedComment: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_input)

//        Log.d("inputvalue", inputTypeFromStart.toString())
//        Log.d("activityvalue", activityTypeFromStart.toString())

        val listView = findViewById<ListView>(R.id.listView)

        val items = arrayOf("Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment")

        val customAdapter = CustomAdapter(this, items)
        listView.adapter = customAdapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            when (selectedItem) {
                "Date" -> showDateDialog()
                "Time" -> showTimeDialog()
                "Duration" -> showDurationDialog()
                "Distance" -> showDistanceDialog()
                "Calories" -> showCaloriesDialog()
                "Heart Rate" -> showHeartRateDialog()
                "Comment" -> showCommentDialog()
            }
        }

        val cancelBtn = findViewById<Button>(R.id.cancelInput)
        cancelBtn.setOnClickListener(){
            finish()
        }
        val saveBtn = findViewById<Button>(R.id.saveInput)
        saveBtn.setOnClickListener(){
            saveSelectedValues()
            finish()
        }
    }

    private fun saveSelectedValues(){
        database = HistoryDatabase.getInstance(this)
        databaseDao = database.historyDataDao
        repository = HistoryRepository(databaseDao)
        historyViewModelFactory = HistoryViewModelFactory(repository)
        historyViewModel = ViewModelProvider(this, historyViewModelFactory).get(HistoryViewModel::class.java)


        val inputTypeFromStart = intent.getIntExtra("inputType", -1)
        val activityTypeFromStart = intent.getIntExtra("activityType", -1)

        val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val unit = sharedPreferences.getString("unitPreference", "km") ?: ""

        if (unit == "miles"){
            selectedDistance = selectedDistance?.times(1.609344)
        }

        var combinedDateTime = stringToCalendar(selectedDate, selectedTime)
        val historyInstance:HistoryData = HistoryData(
            inputType = inputTypeFromStart,
            activityType = activityTypeFromStart,
            dateTime = combinedDateTime,
            duration = selectedDuration?:0.0,
            distance = selectedDistance?:0.0,
            avgPace = 0.0,
            avgSpeed = 0.0,
            calorie = selectedCalories?:0.0,
            heartRate = selectedHeartRate?:0.0,
            comment = selectedComment?:""
            )

        historyViewModel.insert(historyInstance)
//        historyViewModel.deleteAll()
        Toast.makeText(this, "Saved to DB", Toast.LENGTH_SHORT).show()

    }

    fun stringToCalendar(dateString: String, time:String): Calendar {
        val calendar = Calendar.getInstance()
        val dateParts = dateString.split("-")
        if (dateParts.size == 3) {
            val year = dateParts[2].toInt()
            val month = dateParts[0].toInt() - 1 // Calendar months are 0-based
            val day = dateParts[1].toInt()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
        }

        // Parse time
        val timeParts = time.split(":")
        if (timeParts.size == 2) {
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0) // You may set seconds and milliseconds as needed
        }

        return calendar
    }

    fun showDateDialog() {
//        Toast.makeText(this, "hihi", Toast.LENGTH_SHORT).show()
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                selectedDate = "${selectedMonth + 1}-$selectedDayOfMonth-$selectedYear"
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

    fun showTimeDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                selectedTime = "$selectedHour:$selectedMinute"
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()    }

    fun showDurationDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        AlertDialog.Builder(this)
            .setTitle("Duration")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val inputValue = input.text.toString()
                val duration = inputValue.toDoubleOrNull()

                selectedDuration = duration

                if (duration != null && duration > 0) {
                    // valid duration time
                } else {
                    // Invalid duration time
                }
            }
            .setNegativeButton("Cancel", null)
            .show()    }

    fun showDistanceDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        AlertDialog.Builder(this)
            .setTitle("Distance")
            .setView(input)
            .setPositiveButton("OK") {
                _, _ ->
                val inputValue = input.text.toString()
                val distance = inputValue.toDoubleOrNull()

                selectedDistance = distance

                if(distance != null && distance >0){
                    //OK response

                }
                else{
                    //invalid response
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun showCaloriesDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("Calories")
            .setView(input)
            .setPositiveButton("OK"){
                _, _ ->
                val inputValue = input.text.toString()
                val calories = inputValue.toDoubleOrNull()

                selectedCalories = calories
                if (calories != null && calories >0){
                    //valid input
                }
                else{
                    //invalid input
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun showHeartRateDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("Heart Rate")
            .setView(input)
            .setPositiveButton("OK"){
                    _, _ ->
                val inputValue = input.text.toString()
                val heartRate = inputValue.toDoubleOrNull()

                selectedHeartRate = heartRate

                if (heartRate != null && heartRate >0){
                    //valid input
                }
                else{
                    //invalid input
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun showCommentDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        input.hint = "How did it go? Notes here"

        AlertDialog.Builder(this)
            .setTitle("Comment")
            .setView(input)
            .setPositiveButton("OK"){
                    _, _ ->
                val comment = input.text.toString().trim()
                selectedComment = comment

            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
class CustomAdapter(context: AppCompatActivity, private val items: Array<String>) :
    ArrayAdapter<String>(context, R.layout.list_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.list_item, parent, false)

        val textView = rowView.findViewById<TextView>(R.id.listItemTextView)
        textView.text = items[position]

        textView.setOnClickListener {
            val selectedItem = items[position]
            when (selectedItem) {
                "Date" -> (context as StartInput).showDateDialog()
                "Time" -> (context as StartInput).showTimeDialog()
                "Duration" -> (context as StartInput).showDurationDialog()
                "Distance" -> (context as StartInput).showDistanceDialog()
                "Calories" -> (context as StartInput).showCaloriesDialog()
                "Heart Rate" -> (context as StartInput).showHeartRateDialog()
                "Comment" -> (context as StartInput).showCommentDialog()
            }
        }

        return rowView
    }
}