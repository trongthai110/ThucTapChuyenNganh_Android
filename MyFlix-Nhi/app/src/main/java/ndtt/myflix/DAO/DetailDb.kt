package ndtt.myflix.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ndtt.myflix.models.Converters
import ndtt.myflix.models.Results

@Database(entities = [Results::class], version = 1)
@TypeConverters(Converters::class)
abstract class DetailDb : RoomDatabase() {
    abstract fun detailDao(): DetailDao
}
