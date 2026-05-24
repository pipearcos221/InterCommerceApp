package co.com.pipearcos221.intercommerceapp.core.data.mapper

import co.com.pipearcos221.intercommerceapp.core.database.entity.CartEntity
import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product

fun CartEntity.toDomain(): CartItem {
    return CartItem(
        product = Product(
            id = productId,
            title = title,
            category = category,
            thumbnail = imageUrl,
            price = currentPrice,
            description = "",
            discountPercentage = 0.0,
            rating = 0.0,
            stock = 0,
            brand = "",
            images = emptyList()
        ),
        quantity = quantity
    )
}

fun Product.toCartEntity(quantity: Int): CartEntity {
    return CartEntity(
        productId = id,
        title = title,
        category = category,
        imageUrl = thumbnail,
        currentPrice = price,
        originalPrice = if (discountPercentage > 0) price / (1 - (discountPercentage / 100)) else null,
        quantity = quantity
    )
}
