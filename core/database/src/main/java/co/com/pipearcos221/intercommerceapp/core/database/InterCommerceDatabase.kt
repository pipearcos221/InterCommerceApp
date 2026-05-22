package co.com.pipearcos221.intercommerceapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.com.pipearcos221.intercommerceapp.core.database.converter.Converters
import co.com.pipearcos221.intercommerceapp.core.database.dao.ProductDao
import co.com.pipearcos221.intercommerceapp.core.database.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class InterCommerceDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
}
