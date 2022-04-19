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

    private var liveDataOfUser: LiveData<UserModel>? = null
    var liveDataListOfUser: LiveData<List<UserModel>>? = null

    fun insertData(context: Context, firstName: String, lastName: String) {
        UserRepository.insertData(context, firstName, lastName)
    }

    fun getData(context: Context, firstName: String): LiveData<UserModel>? {
        liveDataOfUser = UserRepository.getUserDetails(context, firstName)
        return liveDataOfUser
    }

    fun getAllData(context: Context): LiveData<List<UserModel>>? {
        liveDataListOfUser = UserRepository.getAllUserDetails(context)
        return liveDataListOfUser
    }

    fun deleteAllUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.deleteAllUsers()
        }
    }
}