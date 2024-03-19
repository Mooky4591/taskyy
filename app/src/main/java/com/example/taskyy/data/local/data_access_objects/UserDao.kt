package com.example.taskyy.data.local.data_access_objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskyy.data.local.room_entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("UPDATE user_table SET token = :token, id = :id WHERE email = :email")
    suspend fun update(token: String?, id: String?, email: String)

    @Query("SELECT name FROM user_table WHERE email = :email")
    suspend fun getUserNameByEmail(email: String): String

    @Query("SELECT COUNT() FROM user_table WHERE id = :userId")
    suspend fun doesUserExist(userId: String): Boolean

}