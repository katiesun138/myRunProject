package com.example.asn1

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Settings : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private val COMMENTS_KEY = "comments"

    data class ListItem(val name: String, val action: (() -> Unit)? = null)

    class ListAdapter(private val context: Context, private val items: List<ListItem>) : ArrayAdapter<ListItem>(context, android.R.layout.simple_list_item_1, items) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            val item = items[position]

            (view as TextView).text = item.name

            view.setOnClickListener {
                item.action?.invoke()
            }

            return view
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val optionsListView = view.findViewById<ListView>(R.id.accOptions)
        val addSettingsListView = view.findViewById<ListView>(R.id.addSettingsOptions)
        val miscListView = view.findViewById<ListView>(R.id.miscListViewOptions)

        val optionsItems = listOf(
            ListItem("Name, Email, Class, etc") { showProfilePreferenceDialog() },
            ListItem("Privacy Setting") { showPrivacyDialog() }
        )

        val addSettingsItems = listOf(
            ListItem("Unit Preference") { showUnitPreferenceDialog() },
            ListItem("Comments") { showCommentsDialog() }
        )

        val miscItems = listOf(
            ListItem("Webpage") { showWebpageDialog() }
        )

        val optionsAdapter = ListAdapter(requireContext(), optionsItems)
        val addSettingsAdapter = ListAdapter(requireContext(), addSettingsItems)
        val miscAdapter = ListAdapter(requireContext(), miscItems)

        optionsListView.adapter = optionsAdapter
        addSettingsListView.adapter = addSettingsAdapter
        miscListView.adapter = miscAdapter

        return view
    }



    private fun showProfilePreferenceDialog() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(requireContext(), "Button to go to profle!", Toast.LENGTH_SHORT).show()
    }

    private fun showPrivacyDialog() {

    }

    private fun showUnitPreferenceDialog(){
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.unit_preference_dialog, null)

        val kmRadioButton = dialogView.findViewById<RadioButton>(R.id.radioKm)
        val milesRadioButton = dialogView.findViewById<RadioButton>(R.id.radioMiles)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)

        val userPreference = getUserUnitPreference()
        if (userPreference == "km") {
            kmRadioButton.isChecked = true
        } else if (userPreference == "miles") {
            milesRadioButton.isChecked = true
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Unit Preference")
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val selectedUnitPreference = when (radioGroup.checkedRadioButtonId) {
                    R.id.radioKm -> "km"
                    R.id.radioMiles -> "miles"
                    else -> ""
                }

                if (selectedUnitPreference.isNotEmpty()) {
                    saveUserUnitPreference(selectedUnitPreference)
                    alertDialog.dismiss()
                } else {
                   Toast.makeText(requireContext(), "Please select a unit preference", Toast.LENGTH_SHORT).show()
                }
            }
        }

        alertDialog.show()
    }

    private fun getUserUnitPreference(): String {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("unitPreference", "") ?: ""
    }

    private fun saveUserUnitPreference(selectedUnitPreference: String) {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("unitPreference", selectedUnitPreference)
        editor.apply()
    }


    private fun showCommentsDialog(){
        Toast.makeText(requireContext(), "comments!!!", Toast.LENGTH_SHORT).show()

        val savedComments = sharedPreferences.getString(COMMENTS_KEY, "").toString()

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(savedComments)
        
        AlertDialog.Builder(requireContext())
            .setTitle("Comment")
            .setView(input)
            .setPositiveButton("OK"){
                    _, _ ->
                val comment = input.text.toString().trim()
                saveCommentToSharedPreferences(comment)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveCommentToSharedPreferences(comment: String) {
        val editor = sharedPreferences.edit()
        editor.putString(COMMENTS_KEY, comment)
        editor.apply()
    }

    private fun showWebpageDialog() {
        val url = "https://www.sfu.ca/computing.html"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.android.chrome")
        startActivity(intent)
    }




}

