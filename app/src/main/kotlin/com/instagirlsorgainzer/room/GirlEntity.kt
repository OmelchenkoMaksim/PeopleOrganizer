package com.instagirlsorgainzer.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "girls")
data class GirlEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUrl: String,
    val name: String,
    val url: String,
    val age: Int,
    val hasBoyfriend: Boolean,
    val comments: String,
    val zodiac: String,
    val interaction: String,
    val lastClicked: Long,
    val isFavorite: Boolean
)