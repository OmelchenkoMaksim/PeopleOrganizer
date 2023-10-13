package com.friendsorgainzer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friendsorgainzer.enums.CrashLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import com.friendsorgainzer.room.MainRepository
import com.friendsorgainzer.room.PersonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository) : ViewModel() {
    val allGirls: Flow<List<PersonEntity>> = repository.allPersons

    fun addGirl(name: String, zodiac: String = "", age: Int) {
        val newGirl = PersonEntity(0, name, zodiac, age = age)
        viewModelScope.launch {
            repository.insertPerson(newGirl)
        }
    }

    fun deleteGirl(girl: PersonEntity) {
        viewModelScope.launch {
            repository.deletePerson(girl)
        }
    }

    fun insertDefaultGirls() {
        viewModelScope.launch {
            repository.insertDefaultGirls()
        }
    }

    fun updateGirlZodiac(girlId: Int, zodiacSign: ZodiacSign) {
        viewModelScope.launch {
            repository.updatePersonZodiac(girlId, zodiacSign)
        }
    }

    fun updateGirlAge(girlId: Int, age: Int) {
        viewModelScope.launch {
            repository.updatePersonAge(girlId, age)
        }
    }

    fun updateCrashSelected(id: Int, selectedLevel: CrashLevel) {
        viewModelScope.launch {
            repository.updateCrashSelected(id, selectedLevel)
        }
    }

    fun updateInteractionSelected(id: Int, selectedLevel: InteractionLevel) {
        viewModelScope.launch {
            repository.updateInteractionLevel(id, selectedLevel)
        }
    }

    fun updateGirlComment(girlId: Int, comment: String) {
        viewModelScope.launch {
            repository.updatePersonComment(girlId, comment)
        }
    }

    fun updateGirlUrl(girlId: Int, url: String) {
        viewModelScope.launch {
            repository.updatePersonPhotoUrl(girlId, url)
        }
    }

    fun updateGirlHasBoyfriend(girlId: Int, hasBoyfriend: Boolean) {
        viewModelScope.launch {
            repository.updateInRelations(girlId, hasBoyfriend)
        }
    }

    fun clearRoom() {
        viewModelScope.launch {
            repository.clearDatabase()
        }
    }

}
