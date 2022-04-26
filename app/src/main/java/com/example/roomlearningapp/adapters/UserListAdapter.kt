package com.example.roomlearningapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.roomlearningapp.R
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.viewModel.UserViewModel

private const val TAG = "UserListAdapter"

class UserListAdapter(private var userList: List<UserModel>, private val context: Context) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    val viewModel = UserViewModel()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return UserViewHolder(root, context)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    inner class UserViewHolder(private val binding: View, private val context: Context) :
        RecyclerView.ViewHolder(binding) {
        fun bind(userModel: UserModel) {
            val firstNameString = userModel.firstName
            val lastNameString = userModel.lastName
            binding.findViewById<TextView>(R.id.surname).text = firstNameString
            binding.findViewById<TextView>(R.id.name).text = lastNameString
            binding.findViewById<TextView>(R.id.menu_item)
                .setOnLongClickListener { popupMenus(it, this) }
        }

        // Item menu
        private fun popupMenus(view: View, viewHolder: UserViewHolder): Boolean {
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.item_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.move_up -> {
                        Log.d(TAG, "Move up")
                        true
                    }
                    R.id.move_down -> {
                        Log.d(TAG, "Move down")
                        true
                    }
                    R.id.remove_user -> {
                        AlertDialog.Builder(context)
                            .setPositiveButton("OK") { dialog, _ ->
                                viewModel.deleteUser(getItemByID(viewHolder.adapterPosition))
                                notifyItemRemoved(viewHolder.adapterPosition)
                                Toast.makeText(context, "User deleted!", Toast.LENGTH_SHORT)
                                    .show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setTitle("Delete")
                            .setIcon(R.drawable.warning)
                            .setMessage("Delete this User?")
                            .create()
                            .show()
                        true
                    }
                    R.id.highlight_user -> {
                        viewHolder.itemView
                            .findViewById<LinearLayout>(R.id.text_container)
                            .setBackgroundColor()
                        Log.d(TAG, "Highlight user")
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
            return true
        }
    }

    fun getItemByID(id: Int): UserModel {
        return userList[id]
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}

private fun View.setBackgroundColor() {
    this.setBackgroundColor(Color.parseColor("#03FDFC"))
}
