package com.friendsorgainzer.ui.home.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.friendsorgainzer.room.PersonEntity

class PersonDiffCallback : DiffUtil.ItemCallback<PersonEntity>() {
    override fun areItemsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
        return oldItem.photoLocalUri == newItem.photoLocalUri &&
                oldItem.name == newItem.name &&
                oldItem.url == newItem.url &&
                oldItem.age == newItem.age
    }

    override fun getChangePayload(oldItem: PersonEntity, newItem: PersonEntity): Any? {
        val diffBundle = Bundle()

        if (oldItem.name != newItem.name) {
            diffBundle.putString(KEY_NAME, newItem.name)
        }
        if (oldItem.photoLocalUri != newItem.photoLocalUri) {
            diffBundle.putString(KEY_PHOTO_LOCAL_URI, newItem.photoLocalUri)
        }
        if (oldItem.age != newItem.age) {
            diffBundle.putInt(KEY_AGE, newItem.age)
        }
        if (oldItem.comments != newItem.comments) {
            diffBundle.putString(KEY_COMMENTS, newItem.comments)
        }
        if (oldItem.birthday != newItem.birthday) {
            diffBundle.putString(KEY_BIRTHDAY, newItem.birthday)
        }
        if (oldItem.inRelations != newItem.inRelations) {
            diffBundle.putBoolean(KEY_IN_RELATIONS, newItem.inRelations!!)
        }
        if (oldItem.lastClicked != newItem.lastClicked) {
            diffBundle.putLong(KEY_LAST_CLICKED, newItem.lastClicked)
        }
        if (oldItem.isFavorite != newItem.isFavorite) {
            diffBundle.putBoolean(KEY_IS_FAVORITE, newItem.isFavorite)
        }
        if (oldItem.zodiac != newItem.zodiac) {
            diffBundle.putString(KEY_ZODIAC, newItem.zodiac.name)
        }
        if (oldItem.interaction != newItem.interaction) {
            diffBundle.putString(KEY_INTERACTION, newItem.interaction.name)
        }
        if (oldItem.crushLevel != newItem.crushLevel) {
            diffBundle.putString(KEY_CRUSH_LEVEL, newItem.crushLevel.name)
        }

        return if (diffBundle.size() != 0) diffBundle else null
    }

    companion object Keys {
        const val KEY_NAME = "KEY_NAME"
        const val KEY_PHOTO_LOCAL_URI = "KEY_PHOTO_LOCAL_URI"
        const val KEY_AGE = "KEY_AGE"
        const val KEY_COMMENTS = "KEY_COMMENTS"
        const val KEY_BIRTHDAY = "KEY_BIRTHDAY"
        const val KEY_IN_RELATIONS = "KEY_IN_RELATIONS"
        const val KEY_LAST_CLICKED = "KEY_LAST_CLICKED"
        const val KEY_IS_FAVORITE = "KEY_IS_FAVORITE"
        const val KEY_ZODIAC = "KEY_ZODIAC"
        const val KEY_INTERACTION = "KEY_INTERACTION"
        const val KEY_CRUSH_LEVEL = "KEY_CRUSH_LEVEL"
    }
}
