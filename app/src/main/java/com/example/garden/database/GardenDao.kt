package com.example.garden.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

@Dao
interface GardenDao {
    @Query("SELECT * FROM Garden where GardenProblems=:listName")
    suspend fun getGardenList(listName: String): List<Garden> //получение списка объедененные одним симптомом

    @Query("SELECT * FROM Garden WHERE id = :id")
    suspend fun getById(id: Int): Garden // берем лекарство по ID

    @Query("SELECT GardenProblems FROM Garden") // списки бокового меню
    suspend fun getLists(): List<String>

    @Update
    fun updateGard(garden : Garden) //обновить

    @Delete
    fun deleteGarden(garden: Garden) //удаляем полученный элемент


    //добавляем новый препарат без id
    @Query("INSERT INTO Garden (name, RecommendedDrugs, GardenProblems) VALUES (:name, :RecommendedDrugs, :GardenProblems)")
    fun addGarden(name : String, RecommendedDrugs : String, GardenProblems : String)
}