package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.adapters.PopularItemAdapter
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
    private var categoryList: ArrayList<Category> = ArrayList()
    private lateinit var categoryViewType: String;

    companion object {
        const val MEAL_ID = "com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.easyfood.fragments.categoryName"
        const val CATEGORY_NOT_EXPANDED = "com.example.easyfood.fragments.categoryViewNotExpanded"
        const val CATEGORY_EXPANDED = "com.example.easyfood.fragments.categoryViewExpanded"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //initializations
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = (activity as MainActivity).viewModel;
        popularItemAdapter = PopularItemAdapter()
        categoriesAdapter = CategoriesAdapter(this)
        categoryViewType = CATEGORY_NOT_EXPANDED;


        viewModel.getPopularItem()
        viewModel.getRandomMeal();
        viewModel.getCategories();

        setUpRecyclerData();
        setObserveAbles()
        setListener()

        return binding.root;
    }

    private fun setUpRecyclerData() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemAdapter
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun setObserveAbles() {
        viewModel.observeRandomMealLiveData().observe(this.viewLifecycleOwner) {
            Glide.with(this@HomeFragment)
                .load(it!!.strMealThumb)
                .into(binding.icRandomMeal)
            randomMeal = it;
        }

        viewModel.observePopularItemLiveData().observe(viewLifecycleOwner)
        {
            popularItemAdapter.setMeals(it as ArrayList<MealsByCategory>)
        }

        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) {
            categoryList.clear()
            categoryList.addAll(it as ArrayList<Category>)

            //creating an extension function
            fun <E> ArrayList<E>.filterTillIndex(idx: Int): ArrayList<E> {
                val tempArray: ArrayList<E> = ArrayList();
                for (i in 0 until this.size) {
                    if (i <= idx) {
                        tempArray.add(this[i]);
                    } else
                        break
                }
                return tempArray
            }

            var tempArray = categoryList.filterTillIndex(4)
            var catObj: Category =
                Category("1", "View more..", "", categoryList[tempArray.size - 1].strCategoryThumb);
            tempArray.add(catObj)

            categoriesAdapter.setCategoryList(tempArray)
        }
    }

    private fun setListener() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealDetailsActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)

            startActivity(intent)
        }

        popularItemAdapter.onItemClickListener = {
            val intent = Intent(activity, MealDetailsActivity::class.java)
            intent.putExtra(MEAL_ID, it.idMeal)
            intent.putExtra(MEAL_NAME, it.strMeal)
            intent.putExtra(MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }

        popularItemAdapter.onLongItemClickListener = {
            val mealDescBottomSheetFragment = MealDescBottomSheetFragment.newInstance(it.idMeal)
            mealDescBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }

        categoriesAdapter.onItemClickListener = { category: Category, idx ->
            if (idx == 5 && CATEGORY_NOT_EXPANDED.equals(categoryViewType, false)) {
                categoryViewType = CATEGORY_EXPANDED;
                categoriesAdapter.setCategoryList(categoryList)
                binding.categoriesRecyclerView.smoothScrollToPosition(categoryList.size - 1)
                binding.scrollView.post {
                    binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                }
            } else {
                val intent = Intent(activity, CategoryMealsActivity::class.java)
                intent.putExtra(CATEGORY_NAME, category.strCategory)
                startActivity(intent)
            }
        }

        binding.imgSearch.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, SearchFragment())
                .commit()
        }
    }


    override fun onResume() {
        super.onResume()
//        homeViewModel.getRandomMeal();
    }
}