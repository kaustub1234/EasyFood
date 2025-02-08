package com.example.easyfood.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.easyfood.R
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.MealsByCategory
import com.google.gson.JsonObject

class CategoryMealsAdapter() :
    RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {
    var onItemClickListener: ((MealsByCategory, Int) -> Unit)? = null

    inner class CategoryMealsViewHolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var mealsList = ArrayList<MealsByCategory>()

    fun setMealList(mealsList: List<MealsByCategory>) {
        this.mealsList = mealsList as ArrayList<MealsByCategory>
        Log.d("TAG", this.mealsList.toString())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return mealsList.size;
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {

        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .thumbnail(Glide.with(holder.itemView).asDrawable().load(R.drawable.food_loading_gif))
            .error(R.drawable.more_meal)
            .into(holder.binding.imgMeal)
        holder.binding.mealNameTv.text = mealsList[position].strMeal
        holder.itemView.setOnClickListener {
            onItemClickListener!!.invoke(mealsList[position], position)
        }
    }
}