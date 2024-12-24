package com.example.easyfood.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.PopularItemBinding
import com.example.easyfood.pojo.CategoryMeals

class PopularItemAdapter(): RecyclerView.Adapter<PopularItemAdapter.PopularItemViewHolder>() {
    private var mealList = ArrayList<CategoryMeals>()
    lateinit var onItemClickListener: ((CategoryMeals) -> Unit)

    fun setMeals(mealsList: ArrayList<CategoryMeals>)
    {
        this.mealList = mealsList;
        notifyDataSetChanged()
    }

    class PopularItemViewHolder(val binding:PopularItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularItemViewHolder {
        return PopularItemViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {
        return mealList.size;
    }

    override fun onBindViewHolder(holder: PopularItemViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList.get(position).strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener{
            onItemClickListener.invoke(mealList.get(position))
        }
    }
}