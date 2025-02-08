package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.adapters.MealsAdapter
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealDetailsActivity
import com.example.easyfood.databinding.FragmentFravoritesBinding
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFravoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFravoritesBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        prepareFavoritesRecyclerView()
        mealsAdapter.onItemClickListener = { category: Meal, idx ->
            val intent = Intent(activity, MealDetailsActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, category.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, category.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, category.strMealThumb)
            startActivity(intent)
        }

        observeFavorites()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true;
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition;
                val meal: Meal = mealsAdapter.differ.currentList[position]
                viewModel.deleteMeal(mealsAdapter.differ.currentList[position])

                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(meal)
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favoritesRecyclerView)

        return binding.root;
    }

    private fun prepareFavoritesRecyclerView() {
        mealsAdapter = MealsAdapter();
        binding.favoritesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealsAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity()) {
            mealsAdapter.differ.submitList(it.reversed())
        }
    }
}