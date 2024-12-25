package com.example.easyfood.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.Adapters.CategoryMealsAdapter
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.databinding.CategoryItemBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.viewModel.CategoryMealsViewModel
import kotlin.math.log

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel;
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareCategoryMealsRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this)
        {
            categoryMealsAdapter.setMealList(it)
            binding.categoryCountTv.text = "${intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!}: ${it.size.toString()}"
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