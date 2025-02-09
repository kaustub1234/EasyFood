package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.CategoryList
import com.example.easyfood.pojo.MealByCategoryList
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.pojo.RecentMeals
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query
import roomDb.MealDataBase

class HomeViewModel(private val mealDataBase: MealDataBase) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>();
    private var categoriesLiveData = MutableLiveData<List<Category>>();
    private var bottomSheetMealDetailsLiveData = MutableLiveData<Meal>();
    private var searchMealsLiveData = MutableLiveData<List<Meal>>();
    private val TAG = javaClass.kotlin.simpleName;
    private var favouriteMealsLive = mealDataBase.mealDAO().getAllMeals();
    private var recentMealsLiveData = mealDataBase.recentMealDAO().getRecentMeal();

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    Log.d(TAG, randomMeal.toString())
                    randomMealLiveData.value = randomMeal;
                } else {
                    return;
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }

        })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    categoriesLiveData.value = response.body()!!.categories;
                } else {
                    return;
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val meal: Meal = response.body()!!.meals.first()
                    bottomSheetMealDetailsLiveData.value = meal;
                } else {
                    return;
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    fun getPopularItem() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealByCategoryList> {
                override fun onResponse(
                    call: Call<MealByCategoryList>,
                    response: Response<MealByCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemsLiveData.value = response.body()!!.meals;
                    } else {
                        return;
                    }
                }

                override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                }
            })
    }

    fun searchMeals(searchQuery: String) {
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    searchMealsLiveData.value = response.body()!!.meals;
                } else {
                    return;
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }


    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDataBase.mealDAO().deleteMeal(meal)
        }
    }


    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDataBase.mealDAO().insertMeal(meal)
        }
    }

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData;
    }

    fun observePopularItemLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData;
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData;
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favouriteMealsLive;
    }

    fun observeRecentMealsLiveData(): LiveData<List<RecentMeals>> {
        return recentMealsLiveData;
    }

    fun observeBottomSheetMealDetailsLiveData(): LiveData<Meal> = bottomSheetMealDetailsLiveData;
    fun observeSearchMealsLiveData(): LiveData<List<Meal>> = searchMealsLiveData;
}