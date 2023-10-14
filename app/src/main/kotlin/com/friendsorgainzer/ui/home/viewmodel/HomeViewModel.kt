package com.friendsorgainzer.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friendsorgainzer.enums.CrushLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import com.friendsorgainzer.room.MainRepository
import com.friendsorgainzer.room.PersonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository) : ViewModel() {
    private val currentSortOrder = MutableStateFlow(SortBy.NOTHING)

    val allFriends: Flow<List<PersonEntity>> = repository.allPersons
        .combine(currentSortOrder) { friends, sortOrder ->
            when (sortOrder) {
                SortBy.NOTHING -> friends
                SortBy.NAME -> friends.sortedBy { it.name }
                SortBy.AGE -> friends.sortedBy { it.age }
                SortBy.ID -> friends.sortedBy { it.id }
                SortBy.BIRTHDAY -> friends.sortedBy { it.birthday }
                SortBy.FAVORITE -> friends.sortedBy { it.isFavorite }
                SortBy.LAST_CONNECTION -> friends.sortedBy { it.lastClicked }
                SortBy.CRUSH -> friends.sortedBy { it.crushLevel }
                SortBy.INTERACTION -> friends.sortedBy { it.interaction }
                SortBy.ZODIAC -> friends.sortedBy { it.zodiac }
            }
        }

    fun sortList(sortBy: SortBy) {
        currentSortOrder.value = sortBy
    }


    fun addPerson(name: String, zodiac: String = "", age: Int) {
        val newPerson = PersonEntity(0, name, zodiac, age = age)
        viewModelScope.launch {
            repository.insertPerson(newPerson)
        }
    }

    fun deletePerson(personEntity: PersonEntity) {
        viewModelScope.launch {
            repository.deletePerson(personEntity)
        }
    }

    fun insertDefaultList() {
        viewModelScope.launch {
            repository.insertDefaultAccounts()
        }
    }

    fun updateZodiacSign(id: Int, zodiacSign: ZodiacSign) {
        viewModelScope.launch {
            repository.updatePersonZodiac(id, zodiacSign)
        }
    }

    fun updateAge(id: Int, age: Int) {
        viewModelScope.launch {
            repository.updatePersonAge(id, age)
        }
    }

    fun updateCrushSelected(id: Int, selectedLevel: CrushLevel) {
        viewModelScope.launch {
            repository.updateCrushSelected(id, selectedLevel)
        }
    }

    fun updateInteractionSelected(id: Int, selectedLevel: InteractionLevel) {
        viewModelScope.launch {
            repository.updateInteractionLevel(id, selectedLevel)
        }
    }

    fun updateLastClicked(id: Int, lastClicked: Long) {
        viewModelScope.launch {
            repository.updateLastClicked(id, lastClicked)
        }
    }

    fun updateComment(id: Int, comment: String) {
        viewModelScope.launch {
            repository.updatePersonComment(id, comment)
        }
    }

    fun updateName(id: Int, personName: String) {
        viewModelScope.launch {
            repository.updatePersonName(id, personName)
        }
    }

    fun updatePersonPhotoUrl(id: Int, url: String) {
        viewModelScope.launch {
            repository.updatePersonPhotoUrl(id, url)
        }
    }

    fun updateInRelations(id: Int, inRelations: Boolean) {
        viewModelScope.launch {
            repository.updateInRelations(id, inRelations)
        }
    }

    fun updateBirthday(id: Int, date: String) {
        viewModelScope.launch {
            repository.updateBirthday(id, date)
        }
    }

    fun updateFavorite(id: Int, checked: Boolean) {
        viewModelScope.launch {
            repository.updateFavorite(id, checked)
        }
    }

    fun clearRoom() {
        viewModelScope.launch {
            repository.clearDatabase()
        }
    }


    enum class SortBy {
        NOTHING, ID, NAME, AGE, BIRTHDAY, FAVORITE, LAST_CONNECTION, CRUSH, INTERACTION, ZODIAC
    }
}
