package roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.RecentMeals

@Dao
interface RecentMealsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentMeal(recentMeals: RecentMeals);
    @Query("SELECT * FROM recentMealInfo")
    fun getRecentMeal(): LiveData<List<RecentMeals>>
}