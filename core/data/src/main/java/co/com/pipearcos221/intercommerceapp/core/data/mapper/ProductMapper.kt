package co.com.pipearcos221.intercommerceapp.core.data.mapper

import co.com.pipearcos221.intercommerceapp.core.database.entity.ProductEntity
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.network.dto.ProductDto

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand ?: "",
        category = category,
        thumbnail = thumbnail,
        images = images
    )
}

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand ?: "",
        category = category,
        thumbnail = thumbnail,
        images = images
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = images
    )
}
