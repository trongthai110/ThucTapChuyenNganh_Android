package ndtt.myflix.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity (tableName = "movieList")
data class MovieList(
    @PrimaryKey val id: Int,
    val page: Int,
    val results: ArrayList<Results>,
    val total_results: Int,
    val total_pages: Int
)

class Converters {

    @TypeConverter
    fun listToJson(value: ArrayList<Results>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Results>::class.java).toList()
}