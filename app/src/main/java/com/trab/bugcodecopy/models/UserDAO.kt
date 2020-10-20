package com.trab.bugcodecopy.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDAO {
    @Query("SELECT * FROM user " +
            "WHERE username=:username AND password=:password")
    fun get(username: String, password: String) : User?

    @Insert
    fun register(user: User)

    @Query("SELECT * FROM user")
    fun getUsers(): Array<User>
}