package com.example.easyfood.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recentMealInfo")
data class RecentMeals(
    @PrimaryKey
    val idMeal: String,
    val strMealThumb: String?=null,
    val strMeal: String?=null

)
