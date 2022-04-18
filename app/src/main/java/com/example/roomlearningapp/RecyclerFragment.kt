package com.example.roomlearningapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomlearningapp.adapters.UserListAdapter
import com.example.roomlearningapp.viewModel.UserViewModel

private const val TAG = "RecyclerFragment"

class RecyclerFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = UserListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Getting data from the database and passing to the adapter
        userViewModel.getAllData(requireActivity())!!
            .observe(viewLifecycleOwner) { users ->
                if (users == null) {
                    Log.d(TAG, "Data was not found!")
                } else {
                    users.let {
                        adapter.differ.submitList(users)
                        Log.d(TAG, "Data submitted to adapter!")
                    }
                }
            }
        return view
    }
}