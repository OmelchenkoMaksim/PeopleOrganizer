package com.friendsorgainzer.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.friendsorgainzer.enums.CrashLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUrl: String = "imageUrl",
    val photoUrl: String? = null,
    val photoLocalUri: String? = null,
    val name: String = "person",
    val url: String = "url",
    val age: Int = 0,
    val inRelations: Boolean? = null,
    val comments: String = "",
    val zodiac: ZodiacSign = ZodiacSign.SNAKECHARMER,
    val interaction: InteractionLevel = InteractionLevel.DEFAULT,
    val crashLevel: CrashLevel = CrashLevel.DEFAULT,
    val lastClicked: Long = 0,
    val isFavorite: Boolean = false
)
