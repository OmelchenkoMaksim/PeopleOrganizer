package com.friendsorgainzer.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.friendsorgainzer.enums.CrushLevel
import com.friendsorgainzer.enums.InteractionLevel
import com.friendsorgainzer.enums.ZodiacSign
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) для работы с таблицей "persons".
 * Описывает методы для выполнения CRUD-операций.
 */
@Dao
interface MainDao {

    // --- CRUD Operations: Create ---

    /**
     * Добавляет новую запись человека в таблицу.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity)

    /**
     * Добавляет список в таблицу.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friends: List<PersonEntity>)

    // --- CRUD Operations: Read ---

    /**
     * Получает все записи из таблицы.
     */
    @Query("SELECT * FROM persons")
    fun getAllFriends(): Flow<List<PersonEntity>>

    /**
     * Получает запись человека по её ID.
     */
    @Query("SELECT * FROM persons WHERE id = :personId LIMIT 1")
    fun getPersonById(personId: Int): Flow<PersonEntity?>

    // --- CRUD Operations: Update ---

    @Query("UPDATE persons SET inRelations = :checked WHERE id = :personId")
    suspend fun updateInRelations(personId: Int, checked: Boolean)

    @Query("UPDATE persons SET isFavorite = :checked WHERE id = :id")
    suspend fun updateFavorite(id: Int, checked: Boolean)

    /**
     * Обновляет знак зодиака человека.
     */
    @Query("UPDATE persons SET zodiac = :zodiac WHERE id = :id")
    suspend fun updatePersonZodiac(id: Int, zodiac: ZodiacSign)

    /**
     * Обновляет возраст человека.
     */
    @Query("UPDATE persons SET age = :newAge WHERE id = :personId")
    suspend fun updatePersonAge(personId: Int, newAge: Int)

    @Query("UPDATE persons SET crushLevel = :level WHERE id = :personId")
    suspend fun updateCrushLevel(personId: Int, level: CrushLevel)

    @Query("UPDATE persons SET interaction = :level WHERE id = :personId")
    suspend fun updateInteractionLevel(personId: Int, level: InteractionLevel)

    @Query("UPDATE persons SET lastClicked = :lastClicked WHERE id = :id")
    suspend fun updateLastClicked(id: Int, lastClicked: Long)

    @Query("UPDATE persons SET name = :personName WHERE id = :id")
    suspend fun updatePersonName(id: Int, personName: String)

    @Query("UPDATE persons SET birthday = :date WHERE id = :id")
    suspend fun updateBirthday(id: Int, date: String)

    /**
     * Обновляет комментарий к человеку.
     */
    @Query("UPDATE persons SET comments = :newComment WHERE id = :personId")
    suspend fun updatePersonComment(personId: Int, newComment: String)

    /**
     * Обновляет локальный URI фото человека.
     */
    @Query("UPDATE persons SET photoLocalUri = :photoLocalUri WHERE id = :personId")
    suspend fun updatePersonPhotoLocalUri(personId: Int, photoLocalUri: String)

    /**
     * Обновляет URL фото человека.
     */
    @Query("UPDATE persons SET photoUrl = :photoUrl WHERE id = :personId")
    suspend fun updatePersonPhotoUrl(personId: Int, photoUrl: String)

    // ... другие методы для обновления данных

    // --- CRUD Operations: Delete ---

    /**
     * Удаляет запись человека из таблицы.
     */
    @Delete
    suspend fun deletePerson(personEntity: PersonEntity)

    /**
     * Удаляет все записи из таблицы.
     */
    @Query("DELETE FROM persons")
    suspend fun deleteAllPerson()

    /**
     * Сбрасывает счетчик автоинкремента для таблицы "persons".
     */
    @Query("DELETE FROM sqlite_sequence WHERE name='persons'")
    suspend fun resetAutoIncrement()

}
