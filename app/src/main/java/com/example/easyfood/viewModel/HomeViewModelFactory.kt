package com.example.easyfood.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import roomDb.MealDataBase

class HomeViewModelFactory(private val mealDataBase: MealDataBase):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDataBase) as T
    }


}