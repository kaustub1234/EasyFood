package com.example.easyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.adapters.MealsAdapter
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.databinding.FragmentSearchBinding
import com.example.easyfood.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding;
    private lateinit var viewModel: HomeViewModel;
    private lateinit var searchRecyclerViewAdapter: MealsAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        viewModel = (activity as MainActivity).viewModel

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareSearchRecycleView();

        binding.searchImage.setOnClickListener {
            searchMeal()
        }

        var searchJob: Job? = null
        binding.searchBoxEditText.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeals(it.toString())
            }
        }

        observeSearchedMealsLiveData();
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner) {
            searchRecyclerViewAdapter.differ.submitList(it)
        }
    }

    private fun searchMeal() {
        val searchQuery: String = binding.searchBoxEditText.text.toString();
        if (searchQuery.isNotEmpty()) {
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareSearchRecycleView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.searchResultRecycleView.apply {
            adapter = searchRecyclerViewAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        }
    }
}