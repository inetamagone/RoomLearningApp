package com.example.roomlearningapp.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.repository.UserRepository

class UserViewModel : ViewModel() {

    private var liveDataOfUser: LiveData<List<UserModel>>? = null

    fun insertData(context: Context, firstName: String, lastName: String) {
        UserRepository.insertData(context, firstName, lastName)
    }

    fun getData(context: Context, firstName: String): LiveData<List<UserModel>>? {
        liveDataOfUser = UserRepository.getUserDetails(context, firstName)
        return liveDataOfUser
    }

    fun getAllData(context: Context): LiveData<List<UserModel>>? {
        liveDataOfUser = UserRepository.getAllUserDetails(context)
        return liveDataOfUser
    }
}