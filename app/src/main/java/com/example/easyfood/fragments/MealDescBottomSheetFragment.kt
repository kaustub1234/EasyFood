package com.example.easyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.databinding.FragmentCategoriesBinding
import com.example.easyfood.databinding.FragmentMealDescBottomSheetBinding
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "PARAM1"

class MealDescBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding: FragmentMealDescBottomSheetBinding;
    private lateinit var viewModel: HomeViewModel;

    companion object {
        fun newInstance(mealId: String): MealDescBottomSheetFragment =
            MealDescBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, mealId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMealDescBottomSheetBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        mealId?.let {
            viewModel.getMealById(it)
        }

        observeBottomSheetMeal();

        return binding.root
    }

    private fun onBottomSheetDialogClicked()
    {
        binding.mealDetailsButtomSheet.setOnClickListener{

        }
    }

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMealDetailsLiveData().observe(viewLifecycleOwner) { meal ->
            Glide.with(this)
                .load(meal.strMealThumb)
                .into(binding.mealImage)

            binding.mealLoactionTv.text = meal.strCategory;
            binding.mealNameTv.text = meal.strMeal
        }
    }
}