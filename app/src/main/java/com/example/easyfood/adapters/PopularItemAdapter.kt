package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.easyfood.R
import com.example.easyfood.databinding.PopularItemBinding
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.RecentMeals


class PopularItemAdapter() : RecyclerView.Adapter<PopularItemAdapter.PopularItemViewHolder>() {
    private var mealList = ArrayList<MealsByCategory>()
    lateinit var onItemClickListener: ((MealsByCategory) -> Unit)
    var onLongItemClickListener: ((MealsByCategory) -> Unit)? = null

    fun setMeals(mealsList: ArrayList<MealsByCategory>) {
        this.mealList = mealsList;
        notifyDataSetChanged()
    }

    fun setRecentMeals(recentMealsList: ArrayList<RecentMeals>?) {
        val tempMealList = ArrayList<MealsByCategory>()
        recentMealsList?.forEach { recentMeals ->
            val mealsByCategory: MealsByCategory =
                MealsByCategory(recentMeals.idMeal, recentMeals.strMeal, recentMeals.strMealThumb)

            tempMealList.add(mealsByCategory)
        }

        this.mealList = tempMealList;
        notifyDataSetChanged()
    }

    class PopularItemViewHolder(val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularItemViewHolder {
        return PopularItemViewHolder(
            PopularItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return mealList.size;
    }

    override fun onBindViewHolder(holder: PopularItemViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList.get(position).strMealThumb)
            .thumbnail(Glide.with(holder.itemView).asDrawable().load(R.drawable.food_loading_gif))
            .error(R.drawable.more_meal)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClickListener.invoke(mealList.get(position))
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClickListener?.invoke(mealList[position])
            true
        }
    }

}