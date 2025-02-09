package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.adapters.IngrediantsAdapter
import com.example.easyfood.databinding.ActivityMealDetailsBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.RecentMeals
import com.example.easyfood.viewModel.MealDetailsViewModel
import com.example.easyfood.viewModel.MealViewModelFactory
import com.google.gson.Gson
import com.google.gson.JsonObject
import roomDb.MealDataBase

class MealDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealDetailsBinding
    private lateinit var mealId: String;
    private lateinit var mealThumb: String;
    private lateinit var mealName: String;
    private lateinit var mealDetailsViewModel: MealDetailsViewModel;
    private lateinit var youtubeLink: String
    private var meal: Meal? = null
    private lateinit var ingrediantsAdapter: IngrediantsAdapter;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentRandomMealInfo()

//        mealDetailsViewModel = ViewModelProvider(this@MealDetailsActivity)[MealDetailsViewModel::class.java];
        loadingCase()
        setInfoInView()
        mealDetailsViewModel = ViewModelProvider(
            this@MealDetailsActivity, MealViewModelFactory(MealDataBase.getInstance(this))
        )[MealDetailsViewModel::class.java];
        mealDetailsViewModel.getMealDetail(mealId);
        observerMealDetailsLiveData()
        onClickListeners();
    }

    private fun onClickListeners() {
        binding.imgVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }

        binding.addFavBtn.setOnClickListener {
            meal?.let {
                mealDetailsViewModel.insertMeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_LONG).show();
            }
        }
    }

    private fun observerMealDetailsLiveData() {
        mealDetailsViewModel.observeMealDetailsLiveData().observe(
            this
        ) {
            meal = it
            binding.categoryTv.text = "Category: ${it.strCategory}"
            binding.locationTv.text = "Location: ${it.strArea}"
            binding.instructionDescTv.text = it.strInstructions
            youtubeLink = it.strYoutube.toString()

            setIngrediants()

            it?.let {
                val recentMeals: RecentMeals = RecentMeals(
                    idMeal = it.idMeal, strMealThumb = it.strMealThumb, strMeal = it.strMeal
                )
                mealDetailsViewModel.insertRecentMeal(recentMeals)
            }

            onResponseCase()
        }
    }

    private fun setIngrediants() {
        val gson = Gson()
        val jsonObject: JsonObject = gson.fromJson(gson.toJson(meal), JsonObject::class.java)

        ingrediantsAdapter = IngrediantsAdapter()
        binding.ingrediantsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = ingrediantsAdapter
        }

        jsonObject?.let {
            ingrediantsAdapter.setIngrediantsList(it)
        }

        binding.ingrediantsTv.visibility = View.VISIBLE
        binding.ingrediantsRecyclerView.visibility = View.VISIBLE
    }

    private fun setInfoInView() {
        Glide.with(this@MealDetailsActivity).load(mealThumb).into(binding.imgMealDetail)
        binding.collapsingToolBar.title = mealName
    }

    private fun getIntentRandomMealInfo() {
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase() {
        binding.addFavBtn.visibility = View.INVISIBLE
        binding.instructionDescTv.visibility = View.INVISIBLE
        binding.instructionTv.visibility = View.INVISIBLE
        binding.imgVideo.visibility = View.INVISIBLE
        binding.categoryTv.visibility = View.INVISIBLE
        binding.locationTv.visibility = View.INVISIBLE
        binding.imgVideo.visibility = View.INVISIBLE
        binding.linearProgressBar.visibility = View.VISIBLE
    }

    private fun onResponseCase() {
        binding.addFavBtn.visibility = View.VISIBLE
        binding.instructionDescTv.visibility = View.VISIBLE
        binding.instructionTv.visibility = View.VISIBLE
        binding.imgVideo.visibility = View.VISIBLE
        binding.categoryTv.visibility = View.VISIBLE
        binding.locationTv.visibility = View.VISIBLE
        binding.imgVideo.visibility = View.VISIBLE
        binding.linearProgressBar.visibility = View.INVISIBLE
    }
}