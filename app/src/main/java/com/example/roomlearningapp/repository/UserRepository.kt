package com.example.roomlearningapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.roomlearningapp.database.UserDatabase
import com.example.roomlearningapp.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository {

    companion object {

        private var userDatabase: UserDatabase? = null
        private var userModel: LiveData<UserModel>? = null
        private var userModelList: LiveData<List<UserModel>>? = null

        fun insertData(context: Context, firstName: String, lastName: String) {
            userDatabase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                val userInfo = UserModel(firstName, lastName)
                userDatabase!!.getUserDao().insertData(userInfo)
            }
        }

        fun getUserDetails(context: Context, firstName: String): LiveData<UserModel>? {
            userDatabase = initializeDB(context)

            userModel = userDatabase!!.getUserDao().getUserInfo(firstName)
            return userModel
        }

        fun getAllUserDetails(context: Context): LiveData<List<UserModel>>? {
            userDatabase = initializeDB(context)

            userModelList = userDatabase!!.getUserDao().getAllUsers()
            return userModelList
        }

        suspend fun deleteAllUsers(){
            userDatabase!!.getUserDao().deleteAllUsers()
        }

        private fun initializeDB(context: Context): UserDatabase {
            return UserDatabase.createDatabase(context)
        }
    }
}