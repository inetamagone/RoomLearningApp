package com.example.roomlearningapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomlearningapp.adapters.UserListAdapter
import com.example.roomlearningapp.viewModel.UserViewModel

private const val TAG = "RecyclerFragment"

class RecyclerFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        // Getting data from the database and passing to the adapter
        userViewModel.getAllData(requireActivity())
            .observe(viewLifecycleOwner) { users ->
                if (users == null) {
                    Log.d(TAG, getString(R.string.data_not_found))
                } else {
                    users.let {
                        adapter = UserListAdapter(requireContext())
                        recyclerView.adapter = adapter
                        adapter.setData(users)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        // Add options menu
        setHasOptionsMenu(true)
        return view
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
        AlertDialog.Builder(requireContext())
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                userViewModel.deleteAllUsers()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.all_users_removed),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setTitle(getString(R.string.delete))
            .setIcon(R.drawable.warning)
            .setMessage(getString(R.string.delete_all_users))
            .create().show()
    }
}