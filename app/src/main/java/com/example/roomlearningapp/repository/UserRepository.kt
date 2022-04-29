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

        private lateinit var userDatabase: UserDatabase

        fun insertData(context: Context, userModel: UserModel) {
            userDatabase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                userDatabase.getUserDao().insertData(userModel)
            }
        }

        fun getUserDetails(context: Context, firstName: String): LiveData<UserModel> {
            userDatabase = initializeDB(context)
            return userDatabase.getUserDao().getUserInfo(firstName)
        }

        fun getAllUserDetails(context: Context): LiveData<List<UserModel>> {
            userDatabase = initializeDB(context)
            return userDatabase.getUserDao().getAllUsers()
        }

        suspend fun updateColor(colorIndex: Int, id: Int?) =
            userDatabase.getUserDao().updateColor(colorIndex, id)

        suspend fun updateUser(firstName: String, lastName: String, color: Int, id: Int?) =
            userDatabase.getUserDao().updateUser(firstName, lastName, color, id)

        suspend fun deleteAllUsers() =
            userDatabase.getUserDao().deleteAllUsers()

        suspend fun deleteUser(userModel: UserModel) =
            userDatabase.getUserDao().deleteUser(userModel)

        private fun initializeDB(context: Context): UserDatabase {
            return UserDatabase.createDatabase(context)
        }
    }
}