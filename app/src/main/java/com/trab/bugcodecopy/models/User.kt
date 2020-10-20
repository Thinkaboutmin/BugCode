package com.trab.bugcodecopy.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @ColumnInfo val username: String,
    @ColumnInfo val password: String,
    @ColumnInfo val email: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
