package co.com.pipearcos221.intercommerceapp.core.data.di

import co.com.pipearcos221.intercommerceapp.core.data.di.qualifier.DefaultDispatcher
import co.com.pipearcos221.intercommerceapp.core.data.di.qualifier.IoDispatcher
import co.com.pipearcos221.intercommerceapp.core.data.di.qualifier.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
