package com.example.roomlearningapp.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    fun insertData(context: Context, userModel: UserModel) =
        UserRepository.insertData(context, userModel)

    fun getData(context: Context, firstName: String): LiveData<UserModel> {
        return UserRepository.getUserDetails(context, firstName)
    }

    fun getAllData(context: Context): LiveData<List<UserModel>> {
        return UserRepository.getAllUserDetails(context)
    }

    fun updateColor(colorIndex: Int, id: Int?) =
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.updateColor(colorIndex, id)
        }


    fun updateUser(firstName: String, lastName: String, color: Int, id: Int?) =
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.updateUser(firstName, lastName, color, id)
        }

    fun deleteAllUsers() =
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.deleteAllUsers()
        }

    fun deleteUser(userModel: UserModel) =
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.deleteUser(userModel)
        }
}