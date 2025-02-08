package com.example.easyfood.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.CategoryItemBinding
import com.example.easyfood.pojo.Category
import com.google.gson.JsonObject

class IngrediantsAdapter() :
    RecyclerView.Adapter<IngrediantsAdapter.CategoriesViewHolder>() {
    private var ingrediantsList = ArrayList<Category>()

    fun setIngrediantsList(jsonObject: JsonObject) {
        val newIngrediantsList = ArrayList<Category>()
        for (i in 1..20) {
            if (!jsonObject.get("strIngredient$i").asString.isNullOrEmpty()) {
                newIngrediantsList.add(getTempMealsByCategory(jsonObject, i))
            }
        }

        this.ingrediantsList = newIngrediantsList
        Log.d("TAG", this.ingrediantsList.toString())
        notifyDataSetChanged()
    }

    inner class CategoriesViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    private fun getTempMealsByCategory(jsonObject: JsonObject, position: Int): Category {
        val id = jsonObject.get("idMeal").asString
        val name: String = jsonObject.get("strIngredient$position").asString
        val thumbnail: String =
            "https://www.themealdb.com/images/ingredients/${name.replace(" ", "%20")}-Small.png"


        var tempMealByCategory: Category = Category(id, name,"", thumbnail)
        return tempMealByCategory
    }

    override fun getItemCount(): Int {
        return ingrediantsList.size;
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(ingrediantsList[position].strCategoryThumb)
            .thumbnail(Glide.with(holder.itemView).asDrawable().load(R.drawable.food_loading_gif))
            .error(R.drawable.more_meal)
            .into(holder.binding.imgCategory)

        holder.binding.categoryNameTv.text = ingrediantsList[position].strCategory;
    }

}