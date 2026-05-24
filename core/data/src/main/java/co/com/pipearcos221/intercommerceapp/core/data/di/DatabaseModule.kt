package co.com.pipearcos221.intercommerceapp.core.data.di

import android.content.Context
import androidx.room.Room
import co.com.pipearcos221.intercommerceapp.core.database.InterCommerceDatabase
import co.com.pipearcos221.intercommerceapp.core.database.dao.CartDao
import co.com.pipearcos221.intercommerceapp.core.database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "inter_commerce_db"

    @Provides
    @Singleton
    fun provideInterCommerceDatabase(
        @ApplicationContext context: Context
    ): InterCommerceDatabase {
        return Room.databaseBuilder(
            context,
            InterCommerceDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: InterCommerceDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    @Singleton
    fun provideCartDao(database: InterCommerceDatabase): CartDao {
        return database.cartDao()
    }
}
