package com.example.easyfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.Adapters.FavoritesMealsAdapter
import com.example.easyfood.R
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.databinding.FragmentFravoritesBinding
import com.example.easyfood.viewModel.HomeViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFravoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesMealsAdapter: FavoritesMealsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFravoritesBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        prepareFavoritesRecyclerView()
        observeFavorites()

        return binding.root;
    }

    private fun prepareFavoritesRecyclerView() {
        favoritesMealsAdapter = FavoritesMealsAdapter();
        binding.favoritesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesMealsAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity()) {
            favoritesMealsAdapter.differ.submitList(it.reversed())
        }
    }
}