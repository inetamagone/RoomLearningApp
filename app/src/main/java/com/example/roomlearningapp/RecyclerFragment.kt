package com.example.roomlearningapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomlearningapp.adapters.UserListAdapter
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar

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
        // Add menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = UserListAdapter()
        recyclerView.adapter = adapter

        // Swipe delete method
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val user = adapter.differ.currentList[position]
                userViewModel.deleteUser(user)
                Toast.makeText(
                    requireContext(),
                    "User deleted!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerView)
        }

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
    }

    // Delete all method
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteAllUsers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUsers() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("OK") { _, _ ->
            userViewModel.deleteAllUsers()
            Toast.makeText(
                requireContext(),
                "All users are removed",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete All")
        builder.setMessage("Do you want to delete all users?")
        builder.create().show()
    }
}