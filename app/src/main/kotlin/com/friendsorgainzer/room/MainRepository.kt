package com.friendsorgainzer.room

import com.friendsorgainzer.enums.CrushLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainRepository(private val mainDao: MainDao) {
    val allPersons: Flow<List<PersonEntity>> = fillMainList()

    /**
     * Тут страховочная фильтрация,
     * которая удаляет элементы у которых очень похож урл,
     * она не обязательна, т.к. все работает корректно, но на всякий случай оставлю
     */
    private fun fillMainList() = mainDao.getAllFriends()
        .map { list ->
            val uniqueUrls = mutableMapOf<String, PersonEntity>()
            val toDelete = mutableListOf<PersonEntity>()

            list.forEach { person ->
                val shortUrl = person.url.dropLast(2)
                val existing = uniqueUrls[shortUrl]

                if (existing == null || existing.url.length <= person.url.length) {
                    uniqueUrls[shortUrl] = person

                    // Если уже была другая версия, помечаем её на удаление
                    if (existing != null) {
                        toDelete.add(existing)
                    }
                } else {
                    // Если текущая версия короче или равна, помечаем её на удаление
                    toDelete.add(person)
                }
            }

            // Здесь можно удалить дубликаты из БД
            if (toDelete.isNotEmpty()) {
                // вызов DAO метода для удаления
                toDelete.forEach { personWithDuplicateUrl ->
                    deletePerson(personWithDuplicateUrl)
                }
            }

            // Возвращаем уникальные элементы
            uniqueUrls.values.toList()
        }

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

    suspend fun updatePersonLinkUrl(id: Int, url: String) {
        mainDao.updatePersonLinkUrl(id, url)
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
        val entities = listOf<PersonEntity>(

        )
        mainDao.insertAll(entities)
    }

}
