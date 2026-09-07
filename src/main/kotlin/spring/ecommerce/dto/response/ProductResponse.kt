package spring.ecommerce.dto.response

import spring.ecommerce.model.ProductImage


data class ProductResponse(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val createdBy: String? = null,
    val categoryName: String? = null,
    val images: MutableList<Image>? = mutableListOf(),
){
    data class Image(
        val id: Long? = null,
        val imageUrl: String? = null,
        val displayOrder: Int? = null,
        val isPrimary: Boolean? = null,
    )
}