package com.example.roomlearningapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.roomlearningapp.R
import com.example.roomlearningapp.databinding.ListItemBinding
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.viewModel.UserViewModel

class UserListAdapter(private val userList: List<UserModel>, private val context: Context) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    val viewModel = UserViewModel()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        return UserViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    inner class UserViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userModel: UserModel) {

            // instead of binding. binding. binding. much better to use binding.apply {}
            binding.apply {
                surname.text = userModel.firstName
                name.text = userModel.lastName
                menuItem.setOnLongClickListener { popupMenus(it, this@UserViewHolder) }
            }

            if (userModel.colorPriority == 0) {
                binding.textContainer
                    .setBackgroundColor(context.resources.getColor(R.color.purple_200))
            } else {
                binding.textContainer
                    .setBackgroundColor(context.resources.getColor(R.color.highlight_color))
            }
        }

        // Item menu
        private fun popupMenus(view: View, viewHolder: UserViewHolder): Boolean {
            //again .apply{} can do code better by removing not necessary variable and lot of popupMenus. popupMenus. popupMenus.
            PopupMenu(context, view).apply {
                inflate(R.menu.item_menu)
                setOnMenuItemClickListener {

/*
                Code will be much more readable if you will do it like this. (Define separate functions)

                when (it.itemId) {
                    R.id.move_up -> moveItemUp()
                    R.id.move_down -> moveItemDown()
                    R.id.remove_user -> removeUser()
                }

                Also I didn't check your moveUp/Down realisation but pretty sure it can be done by one common function something like this:

                fun moveItem (isDirectionUp: Boolean) {
                    if (isDirectionUp) {up direction piece of code}
                    else {down direction piece of code}
                    common code
                }

                actually even more readable will be introduce enum direction here instead of the Boolean but let it left
                Boolean for now and I'll show how to change to enum and how to use enum if you not used them before


                */

                    when (it.itemId) {
                        R.id.move_up -> {
                            val currentItem = getItemByID(viewHolder.adapterPosition)
                            val currentItemId = currentItem.id
                            val currentPosition = viewHolder.adapterPosition

                            if (currentPosition != 0) {
                                val currentFirstName = currentItem.firstName
                                val currentLastName = currentItem.lastName
                                val currentColor = currentItem.colorPriority

                                val itemBPosition = currentPosition - 1
                                val itemB = getItemByID(itemBPosition)
                                val itemBid = itemB.id
                                val itemBFirstName = itemB.firstName
                                val itemBLastName = itemB.lastName
                                val itemBColor = itemB.colorPriority

                                currentItem.firstName = itemBFirstName
                                currentItem.lastName = itemBLastName
                                currentItem.colorPriority = itemBColor

                                itemB.firstName = currentFirstName
                                itemB.lastName = currentLastName
                                itemB.colorPriority = currentColor

                                viewModel.updateUser(
                                    itemBFirstName,
                                    itemBLastName,
                                    itemBColor,
                                    currentItemId
                                )
                                viewModel.updateUser(
                                    currentFirstName,
                                    currentLastName,
                                    currentColor,
                                    itemBid
                                )
                                notifyDataSetChanged()
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.list_top_message),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            true
                        }
                        R.id.move_down -> {
                            val listSize = userList.size
                            val currentItem = getItemByID(viewHolder.adapterPosition)
                            val currentItemId = currentItem.id
                            val currentPosition = viewHolder.adapterPosition

                            if (currentPosition < (listSize - 1)) {
                                val currentFirstName = currentItem.firstName
                                val currentLastName = currentItem.lastName
                                val currentColor = currentItem.colorPriority

                                val itemBPosition = currentPosition + 1
                                val itemB = getItemByID(itemBPosition)
                                val itemBId = itemB.id
                                val itemBFirstName = itemB.firstName
                                val itemBLastName = itemB.lastName
                                val itemBColor = itemB.colorPriority

                                currentItem.firstName = itemBFirstName
                                currentItem.lastName = itemBLastName
                                currentItem.colorPriority = itemBColor

                                itemB.firstName = currentFirstName
                                itemB.lastName = currentLastName
                                itemB.colorPriority = currentColor

                                viewModel.updateUser(
                                    itemBFirstName,
                                    itemBLastName,
                                    itemBColor,
                                    currentItemId
                                )
                                viewModel.updateUser(
                                    currentFirstName,
                                    currentLastName,
                                    currentColor,
                                    itemBId
                                )
                                notifyDataSetChanged()
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.list_bottom_message),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
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
                show()
            }

            return true
        }
    }

    fun getItemByID(id: Int): UserModel {
        return userList[id]
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    //Poor piece of code.
    // 1. it can be simplified by using Kotlin "expression body"
    // 2. if it return only 0 or 1 it should be boolean I suppouse
    // 3. Don't want to go deep in code but I don't think that variable name currentColor represent value stored. and function name changeColor also doesn't represent it's functionality.
    fun changeColor(currentColor: Int): Int {
        return if (currentColor == 0) {
            1
        } else {
            0
        }
    }
}
