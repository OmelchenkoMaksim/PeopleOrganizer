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
            ),
            PersonEntity(
                name = "viski_gr2004",
                url = "https://www.instagram.com/viski_gr2004/"
            ),
            PersonEntity(
                name = "rozmarindas",
                url = "https://www.instagram.com/rozmarindas/"
            ),
            PersonEntity(
                name = "dashaafan",
                url = "https://www.instagram.com/dashaafan/"
            ),
            PersonEntity(
                name = "anna_rblk",
                url = "https://www.instagram.com/anna_rblk"
            ),
            PersonEntity(
                name = "aanniee.st",
                url = "https://www.instagram.com/aanniee.st/"
            ),
            PersonEntity(
                name = "aloyyya_",
                url = "https://www.instagram.com/aloyyya_/"
            ),
            PersonEntity(
                name = "zhanna_sheina",
                url = "https://www.instagram.com/zhanna_sheina/"
            ),
            PersonEntity(
                name = "la.kud",
                url = "https://www.instagram.com/la.kud/"
            ),
            PersonEntity(
                name = "kozzzya",
                url = "https://www.instagram.com/_kozzzya_/"
            ),
            PersonEntity(
                name = "is.plysha",
                url = "https://www.instagram.com/is.plysha/"
            ),
            PersonEntity(
                name = "deanadeu",
                url = "https://www.instagram.com/deanadeu/"
            ),
            PersonEntity(
                name = "regina_marinets00.00",
                url = "https://www.instagram.com/regina_marinets00.00/"
            ),
            PersonEntity(
                name = "maria.uzhovskaya",
                url = "https://www.instagram.com/maria.uzhovskaya/"
            ),
            PersonEntity(
                name = "sofita178",
                url = "https://www.instagram.com/sofita178/"
            ),
            PersonEntity(
                name = "irinakurnakova",
                url = "https://www.instagram.com/irinakurnakova/"
            ),
            PersonEntity(
                name = "sss.milena",
                url = "https://www.instagram.com/sss.milena/"
            ),
            PersonEntity(
                name = "arihanez",
                url = "https://www.instagram.com/arihanez/"
            ),
            PersonEntity(
                name = "bi_aurrean",
                url = "https://www.instagram.com/bi_aurrean/"
            ),
            PersonEntity(
                name = "_dsh_m",
                url = "https://www.instagram.com/_dsh_m/"
            ),
            PersonEntity(
                name = "_anya_kkkk",
                url = "https://www.instagram.com/_anya_kkkk/"
            ),
            PersonEntity(
                name = "ase4ka.a",
                url = "https://www.instagram.com/ase4ka.a/"
            ),
            PersonEntity(
                name = "evdoka",
                url = "https://www.instagram.com/__evdoka__/"
            ),
            PersonEntity(
                name = "misssevgul",
                url = "https://www.instagram.com/misssevgul/"
            ),
            PersonEntity(
                name = "sofiamarquise",
                url = "https://www.instagram.com/sofiamarquise/"
            ),
            PersonEntity(
                name = "_lizavetaa__a",
                url = "https://www.instagram.com/_lizavetaa__a/"
            ),
            PersonEntity(
                name = "sasha_asperger",
                url = "https://www.instagram.com/sasha_asperger/"
            ),
            PersonEntity(
                name = "lizarrow1",
                url = "https://www.instagram.com/lizarrow1/"
            ),
            PersonEntity(
                name = "chris_tina_010",
                url = "https://www.instagram.com/chris_tina_010"
            ),
            PersonEntity(
                name = "eloonrha",
                url = "https://www.instagram.com/eloonrha/"
            ),
            PersonEntity(
                name = "miss_kriss_kl",
                url = "https://www.instagram.com/miss_kriss_kl/"
            ),
            PersonEntity(
                name = "baiiushkkina",
                url = "https://www.instagram.com/baiiushkkina/"
            ),
            PersonEntity(
                name = "milenaah__a",
                url = "https://www.instagram.com/milenaah__a/"
            ),
            PersonEntity(
                name = "dishaa__p",
                url = "https://www.instagram.com/dishaa__p"
            ),
            PersonEntity(
                name = "nv.polina.vn",
                url = "https://www.instagram.com/nv.polina.vn/"
            ),
            PersonEntity(
                name = "diana_vlasiuk",
                url = "https://www.instagram.com/diana_vlasiuk/"
            ),
            PersonEntity(
                name = "lerochkajmacenco",
                url = "https://www.instagram.com/lerochkajmacenco/"
            ),
            PersonEntity(
                name = "d.nkfrv",
                url = "https://www.instagram.com/d.nkfrv/"
            ),
            PersonEntity(
                name = "alinkamalinka06",
                url = "https://www.instagram.com/alinkamalinka06/"
            ),
            PersonEntity(
                name = "polyshelkk",
                url = "https://www.instagram.com/polyshelkk/"
            ),
            PersonEntity(
                name = "elizaveta_shv",
                url = "https://www.instagram.com/elizaveta_shv/"
            ),
            PersonEntity(
                name = "kristin___kris",
                url = "https://www.instagram.com/kristin___kris/"
            ),
            PersonEntity(
                name = "kris_khamlova",
                url = "https://www.instagram.com/_kris_khamlova_/"
            ),
            PersonEntity(
                name = "allinaliina",
                url = "https://www.instagram.com/allinaliina"
            ),
            PersonEntity(
                name = "nastya_u27",
                url = "https://www.instagram.com/nastya_u27/"
            ),
            PersonEntity(
                name = "berezutskaya",
                url = "https://www.instagram.com/berezutskaya/"
            ),
            PersonEntity(
                name = "myshka_lelya",
                url = "https://www.instagram.com/_myshka_lelya_/"
            ),
            PersonEntity(
                name = "ninchelass",
                url = "https://www.instagram.com/ninchelass/"
            ),
            PersonEntity(
                name = "polinaa_bu",
                url = "https://www.instagram.com/polinaa_bu/"
            ),
            PersonEntity(
                name = "annlo_oo",
                url = "https://www.instagram.com/annlo_oo/"
            ),
            PersonEntity(
                name = "_kettyyyyy",
                url = "https://www.instagram.com/____kettyyyyy___/"
            ),
            PersonEntity(
                name = "porhunova_zhanna",
                url = "https://www.instagram.com/porhunova_zhanna/"
            ),
            PersonEntity(
                name = "shrmt_li",
                url = "https://www.instagram.com/shrmt_li/"
            ),
            PersonEntity(
                name = "adamovalina",
                url = "https://www.instagram.com/adamovalina/"
            ),
            PersonEntity(
                name = "sofakolenchenko",
                url = "https://www.instagram.com/sofakolenchenko/"
            ),
            PersonEntity(
                name = "sofincha_",
                url = "https://www.instagram.com/sofincha_/"
            ),
            PersonEntity(
                name = "varivarenie",
                url = "https://www.instagram.com/varivarenie/"
            ),
            PersonEntity(
                name = "youswiii",
                url = "https://www.instagram.com/youswiii/"
            ),
            PersonEntity(
                name = "sf_vasileva",
                url = "https://www.instagram.com/sf_vasileva/"
            ),
            PersonEntity(
                name = "_kozzyreva",
                url = "https://www.instagram.com/_kozzyreva/"
            ),
            PersonEntity(
                name = "valeriemaslovva",
                url = "https://www.instagram.com/valeriemaslovva/"
            ),
            PersonEntity(
                name = "student_irishka",
                url = "https://www.instagram.com/student_irishka/"
            ),
            PersonEntity(
                name = "constansia_mak",
                url = "https://www.instagram.com/constansia_mak/"
            ),
            PersonEntity(
                name = "olga.bandit",
                url = "https://www.instagram.com/olga.bandit/"
            ),
            PersonEntity(
                name = "kulumbegovamilana",
                url = "https://www.instagram.com/kulumbegovamilana/"
            ),
            PersonEntity(
                name = "dasha_gr21",
                url = "https://www.instagram.com/dasha_gr21/"
            ),
            PersonEntity(
                name = "nastya_shastiie",
                url = "https://www.instagram.com/nastya_shastiie/"
            ),
            PersonEntity(
                name = "juliamarkovva",
                url = "https://www.instagram.com/juliamarkovva/"
            ),
            PersonEntity(
                name = "nastya_adm0109",
                url = "https://www.instagram.com/nastya_adm0109/"
            ),
            PersonEntity(
                name = "marta_shlapak",
                url = "https://www.instagram.com/marta_shlapak/"
            ),
            PersonEntity(
                name = "svtjulianna",
                url = "https://www.instagram.com/svtjulianna/"
            ),
            PersonEntity(
                name = "polinavolkovaa",
                url = "https://www.instagram.com/polinavolkovaa/"
            ),
            PersonEntity(
                name = "rudenkovalbina",
                url = "https://www.instagram.com/rudenkovalbina/"
            ),
            PersonEntity(
                name = "darinabendas",
                url = "https://www.instagram.com/darinabendas/"
            ),
            PersonEntity(
                name = "kh_sasha",
                url = "https://www.instagram.com/kh_sasha/"
            ),
            PersonEntity(
                name = "arina_na",
                url = "https://www.instagram.com/arina_na/"
            ),
            PersonEntity(
                name = "nastya_shastiie",
                url = "https://www.instagram.com/nastya_shastiie/"
            ),
            PersonEntity(
                name = "rudenkovalbina",
                url = "https://www.instagram.com/rudenkovalbina/"
            ),
            PersonEntity(
                name = "chikamalteza",
                url = "https://www.instagram.com/chikamalteza/"
            ),
            PersonEntity(
                name = "svetik.g.g",
                url = "https://www.instagram.com/svetik.g.g/"
            ),
            PersonEntity(
                name = "milena_kuznetsovaaa",
                url = "https://www.instagram.com/milena_kuznetsovaaa/"
            ),
            PersonEntity(
                name = "ksyusha_tsvetkova",
                url = "https://www.instagram.com/ksyusha_tsvetkova/"
            ),
            PersonEntity(
                name = "marinakvlva",
                url = "https://www.instagram.com/marinakvlva/"
            ),
            PersonEntity(
                name = "kzalre",
                url = "https://www.instagram.com/kzalre/"
            ),
            PersonEntity(
                name = "very.lazy.girl",
                url = "https://www.instagram.com/_.melange_/"
            ),
            PersonEntity(
                name = "sunfrueh",
                url = "https://www.instagram.com/sunfrueh/"
            ),
            PersonEntity(
                name = "karina_ilushina",
                url = "https://www.instagram.com/karina_ilushina/"
            ),
            PersonEntity(
                name = "moshlayaa_polly",
                url = "https://www.instagram.com/moshlayaa_polly/"
            ),
            PersonEntity(
                name = "arixpav",
                url = "https://www.instagram.com/arixpav/"
            ),
            PersonEntity(
                name = "ilona_nadrus",
                url = "https://www.instagram.com/ilona_nadrus/"
            ),
            PersonEntity(
                name = "vilittle_",
                url = "https://www.instagram.com/vilittle_/"
            ),
            PersonEntity(
                name = "__mary_t",
                url = "https://www.instagram.com/__mary_t"
            ),
            PersonEntity(
                name = "maria_extra_dry",
                url = "https://www.instagram.com/maria_extra_dry/"
            ),
            PersonEntity(
                name = "mkrvmaria",
                url = "https://www.instagram.com/mkrvmaria/"
            ),
            PersonEntity(
                name = "borona05",
                url = "https://www.instagram.com/borona05/"
            ),
            PersonEntity(
                name = "yra_slava",
                url = "https://www.instagram.com/yra_slava/"
            ),
            PersonEntity(
                name = "vlasiko",
                url = "https://www.instagram.com/vlasiko/"
            ),
            PersonEntity(
                name = "diveal.v",
                url = "https://www.instagram.com/diveal.v/"
            ),
            PersonEntity(
                name = "molchanova_stasya",
                url = "https://www.instagram.com/molchanova_stasya/"
            ),
            PersonEntity(
                name = "dietwater",
                url = "https://www.instagram.com/_dietwater_/"
            ),
            PersonEntity(
                name = "aufee",
                url = "https://www.instagram.com/_aufee_/"
            ),
            PersonEntity(
                name = "sttezaa",
                url = "https://www.instagram.com/sttezaa/"
            ),
            PersonEntity(
                name = "itswonderfullina",
                url = "https://www.instagram.com/itswonderfullina/"
            ),
            PersonEntity(
                name = "katresea",
                url = "https://www.instagram.com/katresea/"
            )
        )
        mainDao.insertAll(entities)
    }

}
