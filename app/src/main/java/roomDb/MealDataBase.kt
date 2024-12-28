package roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.easyfood.pojo.Meal

@Database(entities = [Meal::class], version = 1)
abstract class MealDataBase:RoomDatabase() {
    abstract fun mealDAO():MealDao

    companion object
    {
        @Volatile
        var INSTANCE:MealDataBase? = null

        fun getInstance(context:Context):MealDataBase
        {

            if(INSTANCE==null)
            {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDataBase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }

            return INSTANCE as MealDataBase
        }
    }
}