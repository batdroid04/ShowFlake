package com.example.showflake.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DBConstants.TABLE_MOVIES)
data class Movie(
    @PrimaryKey
    val id: Long,
    val title: String,
    val overview: String,
    val posterPath: String,
    var isFavorite: Boolean = false
)