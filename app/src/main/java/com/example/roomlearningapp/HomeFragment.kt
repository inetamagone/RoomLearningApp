package com.example.roomlearningapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.roomlearningapp.viewModel.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var firstNameString: String
    private lateinit var lastNameString: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<Button>(R.id.submit_button).setOnClickListener {

            firstNameString = view.findViewById<TextInputEditText>(R.id.first_name).text.toString()
                .trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            lastNameString = view.findViewById<TextInputEditText>(R.id.last_name).text.toString()
                .trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            // Save entries into the database
            when {
                firstNameString.isEmpty() -> {
                    Log.d(TAG, "Please enter your First name")
                }
                lastNameString.isEmpty() -> {
                    Log.d(TAG, "Please enter your Last name")
                }
                else -> {
                    userViewModel.insertData(requireActivity(), firstNameString, lastNameString)
                    Log.d(TAG, "Inserted into database!")
                }
            }

            // Get entries from the database
            userViewModel.getData(requireActivity(), firstNameString)!!
                .observe(viewLifecycleOwner) {
                    if (it == null) {
                        Log.d(TAG, "Data was not found!")
                    } else {
                        requireActivity().findViewById<TextView>(R.id.output).text =
                            "Your name is $firstNameString $lastNameString"
                        Log.d(TAG, "Data got successfully!")
                    }
                }
        }
        return view
    }
}