package com.example.easyfood.viewModel

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {

    @TypeConverter
    fun fromAnyToString(attribute:Any?):String{
        if (attribute==null)
            return "";
        return attribute as String
    }
}