package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.Adapters.PopularItemAdapter
import com.example.easyfood.activities.MealDetailsActivity
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.pojo.CategoryMeals
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding;
    private lateinit var homeViewModel: HomeViewModel;
    private lateinit var randomMeal: Meal;
    private lateinit var popularItemAdapter: PopularItemAdapter

    companion object {
        const val MEAL_ID = "com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.fragments.thumbMeal"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeViewModel = ViewModelProvider(this@HomeFragment)[HomeViewModel::class.java];
        popularItemAdapter = PopularItemAdapter()
        homeViewModel.getPopularItem()
        homeViewModel.getRandomMeal();

        preparePopularItemsAdapter();
        observerRandomMeal();
        observePopularItemsLiveData()
        onRandomMealClicked();
        onPopularItemClicked();
        return binding.root;
    }

    private fun onPopularItemClicked() {
        popularItemAdapter.onItemClickListener = {
            val intent = Intent(activity, MealDetailsActivity::class.java)
            intent.putExtra(MEAL_ID, it.idMeal)
            intent.putExtra(MEAL_NAME, it.strMeal)
            intent.putExtra(MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observePopularItemsLiveData() {
        homeViewModel.observePopularItemLiveData().observe(viewLifecycleOwner)
        {
            popularItemAdapter.setMeals(it as ArrayList<CategoryMeals>)
        }
    }

    private fun preparePopularItemsAdapter() {
        homeViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) {
            binding.recViewMealsPopular.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                adapter = popularItemAdapter
            }
        }
    }

    private fun onRandomMealClicked() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealDetailsActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observerRandomMeal() =
        homeViewModel.observeRandomMealLiveData().observe(this.viewLifecycleOwner) {
            Glide.with(this@HomeFragment)
                .load(it!!.strMealThumb)
                .into(binding.icRandomMeal)
            randomMeal = it;
        }


    override fun onResume() {
        super.onResume()
//        homeViewModel.getRandomMeal();
    }
}