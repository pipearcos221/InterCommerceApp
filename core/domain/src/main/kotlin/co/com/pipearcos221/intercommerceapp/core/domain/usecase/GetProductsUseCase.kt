package co.com.pipearcos221.intercommerceapp.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import co.com.pipearcos221.intercommerceapp.core.domain.util.PriceCalculator.withCalculatedPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<PagingData<Product>> {
        return repository.getProducts().map { pagingData ->
            pagingData.map { it.withCalculatedPrice() }
        }
    }
}
