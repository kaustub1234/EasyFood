package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.pojo.MealByCategoryList
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel:ViewModel() {
    private val TAG = javaClass.kotlin.simpleName;
    val mealsLiveData = MutableLiveData<List<MealsByCategory>>();
    fun getMealsByCategory(categoryName:String)
    {
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object :Callback<MealByCategoryList>
        {
            override fun onResponse(
                call: Call<MealByCategoryList>,
                response: Response<MealByCategoryList>
            ) {
                if (response.body() != null) {
                    mealsLiveData.value= response.body()!!.meals;
                } else {
                    return;
                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }

        })
    }

    fun observeMealsLiveData(): LiveData<List<MealsByCategory>>
    {
        return mealsLiveData
    }

}