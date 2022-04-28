package com.example.roomlearningapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.roomlearningapp.model.UserModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(userModel: UserModel)

    @Query("SELECT * FROM user_table WHERE first_name = :firstName")
    fun getUserInfo(firstName: String?): LiveData<UserModel>

    @Query("SELECT * FROM user_table ORDER BY id DESC")
    fun getAllUsers(): LiveData<List<UserModel>>

    @Query("UPDATE user_table SET color_priority=:color WHERE id = :id")
    suspend fun updateColor(color: Int, id: Int?)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

    @Delete
    suspend fun deleteUser(userModel: UserModel)

}