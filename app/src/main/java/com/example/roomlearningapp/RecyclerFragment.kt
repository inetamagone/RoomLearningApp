package com.example.roomlearningapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomlearningapp.adapters.UserListAdapter
import com.example.roomlearningapp.databinding.FragmentRecyclerBinding
import com.example.roomlearningapp.viewModel.UserViewModel

class RecyclerFragment : Fragment(R.layout.fragment_recycler) {

    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val recyclerView = binding.recyclerView
        // Getting data from the database and passing to the adapter
        userViewModel.getAllData(requireActivity())
            .observe(viewLifecycleOwner) { users ->
                if (users == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.data_not_found),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    users.let {
                        adapter = UserListAdapter(users, requireContext())
                        binding.recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        // Add options menu
        setHasOptionsMenu(true)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}