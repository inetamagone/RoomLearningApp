package com.example.roomlearningapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomlearningapp.R
import com.example.roomlearningapp.model.UserModel

class UserListAdapter(private val userList: List<UserModel>)  : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return UserViewHolder(root)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    class UserViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        fun bind(userModel: UserModel) {
            val firstNameString = userModel.firstName
            val lastNameString = userModel.lastName
            binding.findViewById<TextView>(R.id.first_name).text = firstNameString
            binding.findViewById<TextView>(R.id.last_name).text = lastNameString
        }
    }

    fun getItemByID(id: Int): UserModel {
        return userList[id]
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}