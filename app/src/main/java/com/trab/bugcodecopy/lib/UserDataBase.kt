package com.trab.bugcodecopy.lib

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trab.bugcodecopy.models.User
import com.trab.bugcodecopy.models.UserDAO

@Database(entities = [User::class], version = 2)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDAO(): UserDAO

    companion object {
        private var instance: UserDataBase? = null

        fun getDatabase(context: Context) : UserDataBase {
            val tmpInstance = instance
            if (tmpInstance != null) {
                return tmpInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    UserDataBase::class.java,
                    "users").build()
                this.instance = instance
                return instance
            }
        }
    }
}