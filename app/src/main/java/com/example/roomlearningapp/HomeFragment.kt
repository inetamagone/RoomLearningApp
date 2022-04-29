package com.example.roomlearningapp

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.roomlearningapp.databinding.FragmentHomeBinding
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.viewModel.UserViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var firstNameString: String
    private lateinit var lastNameString: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.submitButton.setOnClickListener() {
            firstNameString = binding.firstName.text.formatting()
            lastNameString = binding.lastName.text.formatting()

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
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.data_not_found),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        binding.output.text = getString(R.string.output_text, it.firstName, it.lastName)
                        binding.firstName
                            .setText("")
                        binding.lastName
                            .setText("")
                    }
                }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun Editable?.formatting(): String {
    return this.toString().trim().capitalize()
}
