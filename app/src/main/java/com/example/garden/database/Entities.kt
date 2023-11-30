package com.example.garden.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
// Сущность таблицы Garden
@Entity(tableName = "Garden",
)
data class Garden(
    @ColumnInfo(name = "name") val name: String,
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "GardenProblems") val GardenProblems: String,
    @ColumnInfo(name = "RecommendedDrugs") val RecommendedDrugs: String,
)