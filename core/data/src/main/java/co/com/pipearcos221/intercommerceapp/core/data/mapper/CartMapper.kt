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
            price = originalPrice,
            discountPercentage = discountPercentage,
            priceWithDiscount = priceWithDiscount,
            description = "",
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
        originalPrice = price,
        discountPercentage = discountPercentage,
        priceWithDiscount = priceWithDiscount,
        quantity = quantity
    )
}
