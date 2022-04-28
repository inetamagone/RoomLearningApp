package com.example.roomlearningapp

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.viewModel.UserViewModel
import com.google.android.material.textfield.TextInputEditText

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var firstNameString: String
    private lateinit var lastNameString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<Button>(R.id.submit_button).setOnClickListener {
            firstNameString =
                view.findViewById<TextInputEditText>(R.id.first_name).text.formatting()
            lastNameString = view.findViewById<TextInputEditText>(R.id.last_name).text.formatting()

            // Save entries into the database
            when {
                firstNameString.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.enter_first_name),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                lastNameString.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.enter_last_name),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    val newUser = UserModel(
                        firstName = firstNameString,
                        lastName = lastNameString,
                        colorPriority = 0
                    )
                    userViewModel.insertData(requireActivity(), newUser)
                }
            }

            // Get entries from the database
            userViewModel.getData(requireActivity(), firstNameString)
                .observe(viewLifecycleOwner) {
                    if (it == null) {
                        Log.d(TAG, getString(R.string.data_not_found))
                    } else {
                        val outputText = getString(R.string.output_text, it.firstName, it.lastName)
                        requireActivity().findViewById<TextView>(R.id.output).text = outputText
                        requireActivity().findViewById<TextInputEditText>(R.id.first_name)
                            .setText("")
                        requireActivity().findViewById<TextInputEditText>(R.id.last_name)
                            .setText("")
                    }
                }
        }
        return view
    }
}

private fun Editable?.formatting(): String {
    return this.toString().trim().capitalize()
}
