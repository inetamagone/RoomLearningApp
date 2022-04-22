package com.example.roomlearningapp.viewModel

import android.content.Context
import android.service.autofill.UserData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomlearningapp.model.UserModel
import com.example.roomlearningapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "UserViewModel"
class UserViewModel : ViewModel() {

    lateinit var liveDataOfUser: LiveData<UserModel>
    lateinit var liveDataListOfUser: LiveData<List<UserModel>>
    var userLiveData: MutableLiveData<MutableList<UserModel>>

    init {
        userLiveData  = MutableLiveData()
    }

    fun insertData(context: Context, firstName: String, lastName: String) {
        UserRepository.insertData(context, firstName, lastName)
    }

    fun getData(context: Context, firstName: String): LiveData<UserModel> {
        liveDataOfUser = UserRepository.getUserDetails(context, firstName)!!
        return liveDataOfUser
    }

    fun getAllData(context: Context): LiveData<List<UserModel>> {
        liveDataListOfUser = UserRepository.getAllUserDetails(context)!!
        return liveDataListOfUser
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.deleteAllUsers()
        }
    }

    fun deleteUser(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.deleteUser(userModel)
        }
    }

    fun updateViewModelForScreenRotation(list: MutableList<UserModel>): MutableLiveData<MutableList<UserModel>> {
        userLiveData.value = list
        return userLiveData
    }

}