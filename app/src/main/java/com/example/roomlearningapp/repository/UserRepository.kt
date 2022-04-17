package com.example.roomlearningapp.repository

import androidx.lifecycle.LiveData
import com.example.roomlearningapp.HomeFragment
import com.example.roomlearningapp.database.UserDatabase
import com.example.roomlearningapp.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository {

    companion object {

        var userDatabase: UserDatabase? = null

        var userModel: LiveData<UserModel>? = null

        fun insertData(fragment: HomeFragment, firstName: String, lastName: String) {
            userDatabase = initializeDB(fragment)

            CoroutineScope(Dispatchers.IO).launch {
                val userInfo = UserModel(firstName, lastName)
                userDatabase!!.getUserDao().insertData(userInfo)
            }
        }

        fun getUserDetails(fragment: HomeFragment, firstName: String): LiveData<UserModel>? {
            userDatabase = initializeDB(fragment)

            userModel = userDatabase!!.getUserDao().getUserInfo(firstName)
            return userModel
        }

        private fun initializeDB(fragment: HomeFragment): UserDatabase {
            return UserDatabase.createDatabase(fragment)
        }
    }
}