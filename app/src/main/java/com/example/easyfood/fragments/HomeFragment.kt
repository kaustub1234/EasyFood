package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.Adapters.CategoriesAdapter
import com.example.easyfood.Adapters.PopularItemAdapter
import com.example.easyfood.R
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealDetailsActivity
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding;
    private lateinit var viewModel: HomeViewModel;
    private lateinit var randomMeal: Meal;
    private lateinit var popularItemAdapter: PopularItemAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.easyfood.fragments.categoryName"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = (activity as MainActivity).viewModel;


        popularItemAdapter = PopularItemAdapter()
        categoriesAdapter = CategoriesAdapter()

        viewModel.getPopularItem()
        viewModel.getRandomMeal();
        viewModel.getCategories();

        preparePopularItemsAdapter();
        prepareCategoriesAdapter();

        observerRandomMeal();
        observePopularItemsLiveData();
        observeCategoriesLiveData();

        onRandomMealClicked();
        onPopularItemClicked();
        onPopularItemLongClicked()
        onCategoryClicked();
        onSearchIconCLicked();

        return binding.root;
    }

    private fun onSearchIconCLicked() {
        binding.imgSearch.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, SearchFragment())
                .commit()
        }
    }

    private fun onCategoryClicked() {
        categoriesAdapter.onItemClickListener = { category: Category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesAdapter() {
        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) {
            categoriesAdapter.setCategoryList(it as ArrayList<Category>)
        }
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

    private fun onPopularItemLongClicked() {
        popularItemAdapter.onLongItemClickListener = {
            val mealDescBottomSheetFragment = MealDescBottomSheetFragment.newInstance(it.idMeal)
            mealDescBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemLiveData().observe(viewLifecycleOwner)
        {
            popularItemAdapter.setMeals(it as ArrayList<MealsByCategory>)
        }
    }

    private fun preparePopularItemsAdapter() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) {
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
        viewModel.observeRandomMealLiveData().observe(this.viewLifecycleOwner) {
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