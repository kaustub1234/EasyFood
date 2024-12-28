package com.example.easyfood.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import roomDb.MealDataBase

class MealDetailsViewModel(val mealDataBase: MealDataBase):ViewModel() {
    private var mealDetailsLiveData = MutableLiveData<Meal>();
    private val TAG = javaClass.kotlin.simpleName;

    fun getMealDetail(id:String)
    {
        RetrofitInstance.api.getMealDetails(id).enqueue(object:Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body()!=null)
                {
                    mealDetailsLiveData.value= response.body()!!.meals[0]
                    Log.d(TAG, mealDetailsLiveData.toString())
                }else
                    return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    fun observeMealDetailsLiveData():LiveData<Meal>
    {
        return mealDetailsLiveData
    }

    fun insertMeal(meal: Meal)
    {
        viewModelScope.launch {
            mealDataBase.mealDAO().insertMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal)
    {
        viewModelScope.launch {
            mealDataBase.mealDAO().deleteMeal(meal)
        }
    }

}