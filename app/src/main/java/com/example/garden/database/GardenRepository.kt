package com.example.garden.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GardenRepository(private val gardenDao: GardenDao) {
    //получение элемента по ид
    suspend fun getById(id: Int): Garden {
        return withContext(Dispatchers.IO) {//для чтения или записи файлов, сетевых операций и т.д.
            return@withContext gardenDao.getById(id) // удобные операторы для комбинирования и управления последовательностями асинхронных операций
        }
    }

    //получение списка объедененных одним симптомом
    suspend fun getGardenList(listName: String): List<Garden> {
        return withContext(Dispatchers.IO) {
            return@withContext gardenDao.getGardenList(listName)
        }
    }

    //получение списка симптомов для бокового списка
    suspend fun getLists(): List<String> {
        return withContext(Dispatchers.IO) {
            return@withContext gardenDao.getLists().toSet()
                .toList() //чтобы боковые списки не повторялись
        }
    }

    //добавляем новый медикамент без id
    suspend fun insertGarden(
        name: String,
        RecommendedDrugs: String,
        GardenProblems: String,

    ) {
        return withContext(Dispatchers.IO) {
            gardenDao.addGarden(
                name,
                RecommendedDrugs,
                GardenProblems,

            )
        }
    }

    //удаляем медикамент
    suspend fun deleteGarden(medicine: Int) {
        return withContext(Dispatchers.IO) {
            val gardenEntity = gardenDao.getById(medicine) //получаем по id
            gardenDao.deleteGarden(gardenEntity) //удаляем через запрос
        }
    }
}