package com.friendsorgainzer.ui.home.adapter

import com.friendsorgainzer.enums.CrushLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import com.friendsorgainzer.room.PersonEntity

interface HomeAdapterBridge {
    fun onAgeEntered(id: Int, age: Int)
    fun onCommentEntered(id: Int, comment: String)
    fun onUrlEntered(id: Int, url: String)
    fun onHasPartnerToggled(id: Int, inRelations: Boolean)
    fun onPhotoClick(id: Int)
    fun onPersonDeleted(person: PersonEntity)
    fun onZodiacSelected(id: Int, selectedSign: ZodiacSign)
    fun onInteractionSelected(id: Int, selectedLevel: InteractionLevel)
    fun onCrushSelected(id: Int, selectedLevel: CrushLevel)
    fun onLastClickedUpdated(id: Int, currentTime: Long)
    fun onFavoriteToggled(id: Int, checked: Boolean)
    fun onNameEntered(id: Int, personName: String)
    fun onBirthdayEntered(id: Int, date: String)
}
