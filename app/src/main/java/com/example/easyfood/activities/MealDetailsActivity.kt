package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealDetailsBinding
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.MealDetailsViewModel

class MealDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealDetailsBinding
    private lateinit var mealId: String;
    private lateinit var mealThumb: String;
    private lateinit var mealName: String;
    private lateinit var mealDetailsViewModel: MealDetailsViewModel;
    private lateinit var youtubeLink:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailsBinding.inflate(layoutInflater)
        mealDetailsViewModel =
            ViewModelProvider(this@MealDetailsActivity)[MealDetailsViewModel::class.java];
        loadingCase()
        setContentView(binding.root)
        getIntentRandomMealInfo()
        mealDetailsViewModel.getMealDetail(mealId);

        observerMealDetailsLiveData()
        binding.imgVideo.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }

        setInfoInView()

    }

    private fun observerMealDetailsLiveData() {
        mealDetailsViewModel.observeMealDetailsLiveData().observe(
            this
        ) {
            binding.categoryTv.text = "Category: ${it.strCategory}"
            binding.locationTv.text = "Location: ${it.strArea}"
            binding.instructionDescTv.text = it.strInstructions
            youtubeLink = it.strYoutube.toString()
            onResponseCase()
        }
    }

    private fun setInfoInView() {
        Glide.with(this@MealDetailsActivity)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolBar.title = mealName
    }

    private fun getIntentRandomMealInfo() {
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase()
    {
        binding.addFavBtn.visibility = View.INVISIBLE
        binding.instructionDescTv.visibility = View.INVISIBLE
        binding.instructionTv.visibility = View.INVISIBLE
        binding.imgVideo.visibility = View.INVISIBLE
        binding.categoryTv.visibility = View.INVISIBLE
        binding.locationTv.visibility = View.INVISIBLE
        binding.imgVideo.visibility = View.INVISIBLE
        binding.linearProgressBar.visibility = View.VISIBLE
    }

    private fun onResponseCase()
    {
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