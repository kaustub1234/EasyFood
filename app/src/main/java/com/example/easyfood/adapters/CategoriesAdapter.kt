package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.easyfood.R
import com.example.easyfood.databinding.CategoryItemBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Category

class CategoriesAdapter(var fragment: Fragment?) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    private var categoriesList = ArrayList<Category>()
    var onItemClickListener: ((Category, Int) -> Unit)? = null

    fun setCategoryList(categoryList: ArrayList<Category>) {
        this.categoriesList = categoryList;
        notifyDataSetChanged();
    }

    inner class CategoriesViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return categoriesList.size;
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoriesList[position].strCategoryThumb)
            .thumbnail(Glide.with(holder.itemView).asDrawable().load(R.drawable.food_loading_gif))
            .error(R.drawable.more_meal)
            .into(holder.binding.imgCategory)

        holder.binding.categoryNameTv.text = categoriesList[position].strCategory;
        holder.itemView.setOnClickListener {
            onItemClickListener!!.invoke(categoriesList[position], position)
        }
    }

}