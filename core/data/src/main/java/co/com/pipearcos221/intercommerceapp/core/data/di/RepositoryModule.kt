package co.com.pipearcos221.intercommerceapp.core.data.di

import co.com.pipearcos221.intercommerceapp.core.data.repository.ProductRepositoryImpl
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para vincular la interfaz del repositorio con su implementación.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
}
