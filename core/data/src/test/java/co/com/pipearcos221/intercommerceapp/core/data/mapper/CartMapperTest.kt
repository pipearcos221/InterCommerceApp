package co.com.pipearcos221.intercommerceapp.core.data.mapper

import co.com.pipearcos221.intercommerceapp.core.database.entity.CartEntity
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class CartMapperTest {

    @Test
    fun `given Product when toCartEntity then map all price fields correctly`() {
        // Given
        val product = Product(
            id = 42,
            title = "Nike Air",
            description = "Desc",
            price = 150.0,
            discountPercentage = 20.0,
            rating = 4.8,
            stock = 10,
            brand = "Nike",
            category = "Shoes",
            thumbnail = "url",
            images = emptyList(),
            priceWithDiscount = 120.0
        )
        val quantity = 3

        // When
        val entity = product.toCartEntity(quantity)

        // Then
        assertEquals(product.id, entity.productId)
        assertEquals(product.title, entity.title)
        assertEquals(product.price, entity.originalPrice, 0.001)
        assertEquals(product.discountPercentage, entity.discountPercentage, 0.001)
        assertEquals(product.priceWithDiscount, entity.priceWithDiscount, 0.001)
        assertEquals(quantity, entity.quantity)
    }

    @Test
    fun `given CartEntity when toDomain then map all price fields correctly`() {
        // Given
        val entity = CartEntity(
            productId = 7,
            title = "Socks",
            category = "Apparel",
            imageUrl = "thumb",
            originalPrice = 10.0,
            discountPercentage = 0.0,
            priceWithDiscount = 10.0,
            quantity = 5
        )

        // When
        val domain = entity.toDomain()

        // Then
        val product = domain.product
        assertEquals(entity.productId, product.id)
        assertEquals(entity.title, product.title)
        assertEquals(entity.originalPrice, product.price, 0.001)
        assertEquals(entity.discountPercentage, product.discountPercentage, 0.001)
        assertEquals(entity.priceWithDiscount, product.priceWithDiscount, 0.001)
        assertEquals(entity.quantity, domain.quantity)
    }
}
