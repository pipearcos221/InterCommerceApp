package co.com.pipearcos221.intercommerceapp.core.data.mapper

import co.com.pipearcos221.intercommerceapp.core.database.entity.CartEntity
import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.data.util.DataConstants

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
            description = DataConstants.DEFAULT_STRING,
            rating = DataConstants.DEFAULT_RATING,
            stock = DataConstants.DEFAULT_STOCK,
            brand = DataConstants.DEFAULT_STRING,
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
