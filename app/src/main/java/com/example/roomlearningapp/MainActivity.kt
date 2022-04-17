package com.example.roomlearningapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.roomlearningapp.viewModel.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var firstNameString: String
    private lateinit var lastNameString: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        findViewById<Button>(R.id.submit_button).setOnClickListener {

            firstNameString = findViewById<TextInputEditText>(R.id.first_name).text.toString()
                .trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            lastNameString = findViewById<TextInputEditText>(R.id.last_name).text.toString()
                .trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            // Save entries to the database
            when {
                firstNameString.isEmpty() -> {
                    Log.d(TAG, "Please enter your First name")
                }
                lastNameString.isEmpty() -> {
                    Log.d(TAG, "Please enter your Last name")
                }
                else -> {
                    userViewModel.insertData(this, firstNameString, lastNameString)
                    Log.d(TAG, "Inserted into database!")
                }
            }

            // Get entries from the database
            userViewModel.getData(this, firstNameString)!!.observe(this, Observer {
                if (it == null) {
                    Log.d(TAG, "Data was not found!")
                } else {
                    findViewById<TextView>(R.id.output).text =
                        "Your name is $firstNameString $lastNameString"
                    Log.d(TAG, "Data got successfully!")
                }
            })
        }
    }
}