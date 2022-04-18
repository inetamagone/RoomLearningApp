package com.example.roomlearningapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomlearningapp.R
import com.example.roomlearningapp.model.UserModel

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private val userComparatorDifferCallback = object : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.lastName == newItem.lastName
        }
    }

    val differ = AsyncListDiffer(this, userComparatorDifferCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return UserViewHolder(root)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    class UserViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        fun bind(userModel: UserModel) {
            val firstNameString = userModel.firstName
            val lastNameString = userModel.lastName
            binding.findViewById<TextView>(R.id.first_name).text = firstNameString
            binding.findViewById<TextView>(R.id.last_name).text = lastNameString
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}