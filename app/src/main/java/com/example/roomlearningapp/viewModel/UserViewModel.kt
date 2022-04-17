package com.example.roomlearningapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.roomlearningapp.HomeFragment
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.repository.UserRepository

class UserViewModel : ViewModel() {

    var liveDataOfUser: LiveData<UserModel>? = null

    fun insertData(fragment: HomeFragment, firstName: String, lastName: String) {
        UserRepository.insertData(fragment, firstName, lastName)
    }

    fun getData(fragment: HomeFragment, firstName: String): LiveData<UserModel>? {
        liveDataOfUser = UserRepository.getUserDetails(fragment, firstName)
        return liveDataOfUser
    }
}