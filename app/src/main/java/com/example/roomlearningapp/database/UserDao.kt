package com.example.roomlearningapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.roomlearningapp.model.UserModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(userModel: UserModel)

    @Query("SELECT * FROM user_table WHERE first_name = :firstName")
    fun getUserInfo(firstName: String?): LiveData<List<UserModel>>

    @Query("SELECT * FROM user_table")
    fun selectAllUsers(): LiveData<List<UserModel>>

    @Query("SELECT * FROM user_table ORDER BY id DESC")
    fun getAllUsers(): LiveData<List<UserModel>>
}