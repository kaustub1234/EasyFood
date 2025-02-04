package com.example.easyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
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
        noContentView("Search for your favorite meals here...")
        Glide.with(view).asGif().load(R.drawable.food_loading_gif).into(binding.noContentImg)

        binding.searchImage.setOnClickListener {
            noContentView("Search for your favorite meals here...")
            binding.searchBoxEditText.setText("");
        }

        var searchJob: Job? = null
        binding.searchBoxEditText.doAfterTextChanged {
            if (it.toString().isEmpty()) {
                searchJob?.cancel()
                binding.searchImage.visibility = View.INVISIBLE
                noContentView("Search for your favorite meals here...")
            } else {
                binding.searchImage.visibility = View.VISIBLE
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(300)
                    viewModel.searchMeals(it.toString())
                }
            }
        }

        observeSearchedMealsLiveData();
    }


    fun noContentView(msg: String) {
        binding.apply {
            searchResultRecycleView.visibility = View.INVISIBLE
            noContentLinearLayout.visibility = View.VISIBLE
            noContentMsg.text = msg
        }
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                searchRecyclerViewAdapter.differ.submitList(it)
                binding.searchResultRecycleView.visibility = View.VISIBLE
                binding.noContentLinearLayout.visibility = View.INVISIBLE
            } else {
                noContentView("No such meal found...")
            }
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