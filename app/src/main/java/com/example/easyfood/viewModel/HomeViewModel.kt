package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.CategoryList
import com.example.easyfood.pojo.MealByCategoryList
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel:ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>();
    private var categoriesLiveData = MutableLiveData<List<Category>>();
    private val TAG = javaClass.kotlin.simpleName;

    fun getRandomMeal()
    {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    Log.d(TAG, randomMeal.toString())
                    randomMealLiveData.value=randomMeal;
                } else {
                    return;
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }

        })
    }

    fun getCategories()
    {
        RetrofitInstance.api.getCategories().enqueue(object:Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    categoriesLiveData.value= response.body()!!.categories;
                } else {
                    return;
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    fun getPopularItem()
    {
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object:Callback<MealByCategoryList>{
            override fun onResponse(call: Call<MealByCategoryList>, response: Response<MealByCategoryList>) {
                if (response.body() != null) {
                    popularItemsLiveData.value= response.body()!!.meals;
                } else {
                    return;
                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    fun observeRandomMealLiveData():LiveData<Meal>
    {
        return randomMealLiveData;
    }
    fun observePopularItemLiveData():LiveData<List<MealsByCategory>>
    {
        return popularItemsLiveData;
    }
    fun observeCategoriesLiveData():LiveData<List<Category>>
    {
        return categoriesLiveData;
    }
}