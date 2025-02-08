package com.example.easyfood.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.adapters.CategoryMealsAdapter
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel;
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareCategoryMealsRecyclerView()

        categoryMealsAdapter.onItemClickListener = { category: MealsByCategory, idx ->
            val intent = Intent(this, MealDetailsActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, category.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, category.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, category.strMealThumb)
            startActivity(intent)
        }

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this)
        {
            categoryMealsAdapter.setMealList(it)
            binding.categoryCountTv.text =
                "${intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!}: ${it.size.toString()}"
        }
    }

    private fun prepareCategoryMealsRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.mealsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }


}