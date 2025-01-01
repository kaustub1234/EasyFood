package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.Adapters.CategoriesAdapter
import com.example.easyfood.R
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.databinding.FragmentCategoriesBinding
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.pojo.Category
import com.example.easyfood.viewModel.HomeViewModel
import roomDb.MealDataBase

/**
 * A simple [Fragment] subclass.
 * Use the [CategoriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding;
    private lateinit var categoriesAdapter: CategoriesAdapter;
    private lateinit var viewModel: HomeViewModel;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        prepareRecyclerView();
        onCategoryClicked()
        observeCategoriesLiveData();

        return binding.root;
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) {
            categoriesAdapter.setCategoryList(it as ArrayList<Category>)
        }
    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun onCategoryClicked() {
        categoriesAdapter.onItemClickListener = {category: Category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }
}