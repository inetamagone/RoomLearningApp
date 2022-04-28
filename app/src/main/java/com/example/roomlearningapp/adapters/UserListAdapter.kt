package com.example.roomlearningapp.adapters

import android.app.AlertDialog
import android.content.Context
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

class UserListAdapter(private val context: Context) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private var userList = emptyList<UserModel>()
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

            if (userModel.colorPriority == 0) {
                binding.findViewById<LinearLayout>(R.id.text_container)
                    .setBackgroundColor(context.resources.getColor(R.color.purple_200))
            } else {
                binding.findViewById<LinearLayout>(R.id.text_container)
                    .setBackgroundColor(context.resources.getColor(R.color.highlight_color))
            }
        }

        // Item menu
        private fun popupMenus(view: View, viewHolder: UserViewHolder): Boolean {
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.item_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.move_up -> {
                        val currentItem = getItemByID(viewHolder.adapterPosition)
                        val currentPosition = viewHolder.adapterPosition

                        if (currentPosition != 0) {
                            val currentFirstName = currentItem.firstName
                            val currentLastName = currentItem.lastName
                            val currentColor = currentItem.colorPriority

                            val itemBPosition = currentPosition - 1
                            val itemB = getItemByID(itemBPosition)
                            val itemBfirstName = itemB.firstName
                            val itemBlastName = itemB.lastName
                            val itemBcolor = itemB.colorPriority

                            currentItem.firstName = itemBfirstName
                            currentItem.lastName = itemBlastName
                            currentItem.colorPriority = itemBcolor

                            itemB.firstName = currentFirstName
                            itemB.lastName = currentLastName
                            itemB.colorPriority = currentColor

                            notifyDataSetChanged()
                            Log.d(TAG, "Move up")
                        } else {
                            Log.d(TAG, "This user is already on the top of the list")
                        }

                        true
                    }
                    R.id.move_down -> {
                        val listSize = userList.size
                        val currentItem = getItemByID(viewHolder.adapterPosition)
                        val currentPosition = viewHolder.adapterPosition
                        Log.d(TAG, "currentPosition, $currentPosition")

                        if (currentPosition < (listSize - 1)) {
                            val currentFirstName = currentItem.firstName
                            val currentLastName = currentItem.lastName
                            val currentColor = currentItem.colorPriority

                            val itemBPosition = currentPosition + 1
                            val itemB = getItemByID(itemBPosition)
                            val itemBfirstName = itemB.firstName
                            val itemBlastName = itemB.lastName
                            val itemBcolor = itemB.colorPriority

                            currentItem.firstName = itemBfirstName
                            currentItem.lastName = itemBlastName
                            currentItem.colorPriority = itemBcolor

                            itemB.firstName = currentFirstName
                            itemB.lastName = currentLastName
                            itemB.colorPriority = currentColor

                            notifyDataSetChanged()
                            Log.d(TAG, "Move down")
                        } else {
                            Log.d(TAG, "This user is on the bottom of the list")
                        }
                        true
                    }
                    R.id.remove_user -> {
                        AlertDialog.Builder(context)
                            .setPositiveButton(context.resources.getString(R.string.ok)) { dialog, _ ->
                                viewModel.deleteUser(getItemByID(viewHolder.adapterPosition))
                                notifyItemRemoved(viewHolder.adapterPosition)
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.user_deleted),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                dialog.dismiss()
                            }
                            .setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setTitle(context.getString(R.string.delete))
                            .setIcon(R.drawable.warning)
                            .setMessage(context.getString(R.string.delete_this_user))
                            .create()
                            .show()
                        true
                    }
                    R.id.highlight_user -> {
                        val selectedId = getItemByID(viewHolder.adapterPosition).id
                        val currentColor = getItemByID(viewHolder.adapterPosition).colorPriority
                        val newColor = changeColor(currentColor)
                        viewModel.updateColor(newColor, selectedId)
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

    fun changeColor(currentColor: Int): Int {
        return if (currentColor == 0) {
            1
        } else {
            0
        }
    }

    fun setData(userList: List<UserModel>) {
        this.userList = userList
        notifyDataSetChanged()
    }
}
