package com.example.roomlearningapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.roomlearningapp.R
import com.example.roomlearningapp.databinding.ListItemBinding
import com.example.roomlearningapp.model.MoveDirection
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.viewModel.UserViewModel
import java.lang.reflect.Field


class UserListAdapter(private val userList: List<UserModel>, private val context: Context) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private val viewModel = UserViewModel()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        return UserViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    inner class UserViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userModel: UserModel) {
            binding.apply {
                surname.text = userModel.firstName
                name.text = userModel.lastName
                menuItem.setOnLongClickListener { popupMenus(it, this@UserViewHolder) }
            }

            when {
                !userModel.colorHighlighted -> {
                    binding.textContainer
                        .setBackgroundColor(context.resources.getColor(R.color.purple_200))
                }
                else -> {
                    binding.textContainer
                        .setBackgroundColor(context.resources.getColor(R.color.highlight_color))
                }
            }
        }

        // Item menu
        private fun popupMenus(view: View, viewHolder: UserViewHolder): Boolean {
            val popupMenus = PopupMenu(context, view)
            popupMenus.apply {
                inflate(R.menu.item_menu)
                setForceShowIcon(popupMenus)
                setOnMenuItemClickListener {
                    val isDirectionUp: Boolean
/*              Code will be much more readable if you will do it like this. (Define separate functions)

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
                            moveItem(MoveDirection.UP, viewHolder)
                            true
                        }
                        R.id.move_down -> {
                            moveItem(MoveDirection.DOWN, viewHolder)
                            true
                        }
                        R.id.remove_user -> {
                            removeUser(viewHolder)
                            true
                        }
                        R.id.highlight_user -> {
                            highlightUser(viewHolder)
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

    fun setForceShowIcon(popupMenu: PopupMenu) {
        try {
            val fields: Array<Field> = popupMenu.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper: Any = field.get(popupMenu) as Any
                    val classPopupHelper = Class.forName(
                        menuPopupHelper
                            .javaClass.name
                    )
                    val setForceIcons = classPopupHelper.getMethod(
                        "setForceShowIcon", Boolean::class.javaPrimitiveType
                    )
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun getItemByID(id: Int): UserModel {
        return userList[id]
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    private fun moveItem(direction: MoveDirection, viewHolder: UserViewHolder) {
        val currentPosition = viewHolder.adapterPosition
        val listSize = userList.size
        val itemBPosition: Int

        when (direction) {
            MoveDirection.UP ->
                when {
                    currentPosition != 0 -> {
                        itemBPosition = currentPosition - 1
                        updateMovedUsers(itemBPosition, viewHolder)
                    }
                    else -> Toast.makeText(
                        context,
                        context.getString(R.string.list_top_message),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            MoveDirection.DOWN ->
                when {
                    currentPosition < (listSize - 1) -> {
                        itemBPosition = currentPosition + 1
                        updateMovedUsers(itemBPosition, viewHolder)
                    }
                    else -> Toast.makeText(
                        context,
                        context.getString(R.string.list_bottom_message),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
        }
    }

    private fun updateMovedUsers(itemBPosition: Int, viewHolder: UserListAdapter.UserViewHolder) {
        val currentItem = getItemByID(viewHolder.adapterPosition)
        val currentItemId = currentItem.id

        val currentFirstName = currentItem.firstName
        val currentLastName = currentItem.lastName
        val currentHighlightState = currentItem.colorHighlighted

        val itemB = getItemByID(itemBPosition)
        val itemBid = itemB.id
        val itemBFirstName = itemB.firstName
        val itemBLastName = itemB.lastName
        val itemBHighlightState = itemB.colorHighlighted

        currentItem.firstName = itemBFirstName
        currentItem.lastName = itemBLastName
        currentItem.colorHighlighted = itemBHighlightState

        itemB.firstName = currentFirstName
        itemB.lastName = currentLastName
        itemB.colorHighlighted = currentHighlightState

        viewModel.updateUser(
            itemBFirstName,
            itemBLastName,
            itemBHighlightState,
            currentItemId
        )
        viewModel.updateUser(
            currentFirstName,
            currentLastName,
            currentHighlightState,
            itemBid
        )
        notifyDataSetChanged()
    }


    private fun removeUser(viewHolder: UserListAdapter.UserViewHolder) {
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
    }

    fun highlightUser(viewHolder: UserListAdapter.UserViewHolder) {
        val selectedId = getItemByID(viewHolder.adapterPosition).id
        val currentColorHighlighted = getItemByID(viewHolder.adapterPosition).colorHighlighted
        val newColorHighlighted = isUserHighlighted(currentColorHighlighted)
        viewModel.updateColor(newColorHighlighted, selectedId)
    }

    //Poor piece of code.
// 1. it can be simplified by using Kotlin "expression body"
// 2. if it return only 0 or 1 it should be boolean I suppouse
// 3. Don't want to go deep in code but I don't think that variable name currentColor represent value stored. and function name changeColor also doesn't represent it's functionality.
    private fun isUserHighlighted(currentColorHighlighted: Boolean): Boolean =
        when (currentColorHighlighted) {
            false -> {
                true
            }
            true -> {
                false
            }
        }

}
