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
    private val shouldResort = MutableStateFlow(true)

    val allFriends: Flow<List<PersonEntity>> = repository.allPersons
        .combine(currentSortOrder) { friends, sortOrder ->
            if (!shouldResort.value) {
                return@combine friends // если не нужно пересортировывать, возвращаем список как есть
            }
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
        shouldResort.value = true
    }

    private suspend fun <T> withDatabaseUpdate(block: suspend () -> T): T {
        val result = block()
        shouldResort.value = false
        return result
    }

    fun addPerson(name: String, url: String = "https://") {
        val newPerson = PersonEntity(name = name, url = url)
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
            withDatabaseUpdate {
                repository.updatePersonZodiac(id, zodiacSign)
            }
        }
    }

    fun updateAge(id: Int, age: Int) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updatePersonAge(id, age)
            }
        }
    }

    fun updateCrushSelected(id: Int, selectedLevel: CrushLevel) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updateCrushSelected(id, selectedLevel)
            }
        }
    }

    fun updateInteractionSelected(id: Int, selectedLevel: InteractionLevel) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updateInteractionLevel(id, selectedLevel)
            }
        }
    }

    fun updateLastClicked(id: Int, lastClicked: Long) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updateLastClicked(id, lastClicked)
            }
        }
    }

    fun updateComment(id: Int, comment: String) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updatePersonComment(id, comment)
            }
        }
    }

    fun updateName(id: Int, personName: String) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updatePersonName(id, personName)
            }
        }
    }

    fun updatePersonPhotoUrl(id: Int, url: String) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updatePersonPhotoUrl(id, url)
            }
        }
    }

    fun updateInRelations(id: Int, inRelations: Boolean) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updateInRelations(id, inRelations)
            }
        }
    }

    fun updateWrittenTo(id: Int, writtenTo: Boolean) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updateWrittenTo(id, writtenTo)
            }
        }
    }

    fun updateReceivedReply(id: Int, receivedReply: Boolean) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updateReceivedReply(id, receivedReply)
            }
        }
    }

    fun updateBirthday(id: Int, date: String) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updateBirthday(id, date)
            }
        }
    }

    fun updateFavorite(id: Int, checked: Boolean) {
        viewModelScope.launch {
            withDatabaseUpdate {
                repository.updateFavorite(id, checked)
            }
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
