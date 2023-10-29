package com.friendsorgainzer.room

import com.friendsorgainzer.enums.CrushLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import kotlinx.coroutines.flow.Flow

class MainRepository(private val mainDao: MainDao) {
    val allPersons: Flow<List<PersonEntity>> = mainDao.getAllFriends()

    suspend fun insertPerson(personEntity: PersonEntity) {
        mainDao.insertPerson(personEntity)
    }

    fun getPersonById(id: Int): Flow<PersonEntity?> {
        return mainDao.getPersonById(id)
    }

    suspend fun updatePersonPhotoLocalUri(id: Int, photoLocalUri: String) {
        mainDao.updatePersonPhotoLocalUri(id, photoLocalUri)
    }

    suspend fun deletePerson(personEntity: PersonEntity) {
        mainDao.deletePerson(personEntity)
    }

    suspend fun updatePersonPhotoUrl(id: Int, url: String) {
        mainDao.updatePersonPhotoUrl(id, url)
    }

    suspend fun updateLastClicked(id: Int, lastClicked: Long) {
        mainDao.updateLastClicked(id, lastClicked)
    }

    suspend fun updateCrushSelected(id: Int, level: CrushLevel) {
        mainDao.updateCrushLevel(id, level)
    }

    suspend fun updateInteractionLevel(id: Int, level: InteractionLevel) {
        mainDao.updateInteractionLevel(id, level)
    }


    suspend fun updateInRelations(id: Int, inRelations: Boolean) {
        mainDao.updateInRelations(id, inRelations)
    }

    suspend fun updateWrittenTo(id: Int, writtenTo: Boolean) {
        mainDao.updateWrittenTo(id, writtenTo)
    }

    suspend fun updateReceivedReply(id: Int, receivedReply: Boolean) {
        mainDao.updateReceivedReply(id, receivedReply)
    }

    suspend fun updateFavorite(id: Int, checked: Boolean) {
        mainDao.updateFavorite(id, checked)
    }

    suspend fun updatePersonZodiac(id: Int, zodiac: ZodiacSign) {
        mainDao.updatePersonZodiac(id, zodiac)
    }


    suspend fun updatePersonAge(id: Int, newAge: Int) {
        mainDao.updatePersonAge(id, newAge)
    }

    suspend fun updateBirthday(id: Int, date: String) {
        mainDao.updateBirthday(id, date)
    }

    suspend fun updatePersonName(id: Int, personName: String) {
        mainDao.updatePersonName(id, personName)
    }

    suspend fun updatePersonComment(id: Int, newComment: String) {
        mainDao.updatePersonComment(id, newComment)
    }

    /**
     * Полная очистка Рума
     */
    suspend fun clearDatabase() {
        mainDao.deleteAllPerson()
        mainDao.resetAutoIncrement()
    }

    suspend fun insertDefaultAccounts() {
        val entities = listOf(
            PersonEntity(
                name = "viski_gr2004",
                url = "https://www.instagram.com/viski_gr2004/",
            ),
            PersonEntity(
                name = "rozmarindas",
                url = "https://www.instagram.com/rozmarindas/",
            ),
            PersonEntity(
                name = "dashaafan",
                url = "https://www.instagram.com/dashaafan/",
            ),
            PersonEntity(
                name = "anna_rblk",
                url = "https://www.instagram.com/anna_rblk",
            ),
            PersonEntity(
                name = "aanniee.st",
                url = "https://www.instagram.com/aanniee.st/",
            ),
            PersonEntity(
                name = "aloyyya_",
                url = "https://www.instagram.com/aloyyya_/",
            ),
            PersonEntity(
                name = "zhanna_sheina",
                url = "https://www.instagram.com/zhanna_sheina/",
            ),
            PersonEntity(
                name = "la.kud",
                url = "https://www.instagram.com/la.kud/",
            ),
            PersonEntity(
                name = "_kozzzya_",
                url = "https://www.instagram.com/_kozzzya_/",
            ),
            PersonEntity(
                name = "is.plysha",
                url = "https://www.instagram.com/is.plysha/",
            ),
            PersonEntity(
                name = "deanadeu",
                url = "https://www.instagram.com/deanadeu/",
            )
        )
        mainDao.insertAll(entities)
    }

}
