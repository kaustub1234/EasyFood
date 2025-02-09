package roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.RecentMeals
import com.example.easyfood.viewModel.MealTypeConverter

@Database(entities = [Meal::class, RecentMeals::class], version = 2)
@TypeConverters(MealTypeConverter::class)
abstract class MealDataBase : RoomDatabase() {
    abstract fun mealDAO(): MealDao
    abstract fun recentMealDAO(): RecentMealsDao

    companion object {
        @Volatile
        var INSTANCE: MealDataBase? = null

        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE recentMealInfo (
                idMeal TEXT PRIMARY KEY NOT NULL,
                strMeal TEXT,
                strMealThumb TEXT
            )
        """.trimIndent()
                )
            }
        }

        @Synchronized
        fun getInstance(context: Context): MealDataBase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDataBase::class.java,
                    "meal.db"
                )
                    .addMigrations(migration_1_2)
                    .build()
            }

            return INSTANCE as MealDataBase
        }


    }
}