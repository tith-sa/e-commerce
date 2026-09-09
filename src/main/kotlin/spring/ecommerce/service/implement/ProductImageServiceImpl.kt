package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.ProductImageRequest
import spring.ecommerce.dto.request.UpdatedProductImageRequest
import spring.ecommerce.dto.response.ProductImageResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.ProductImage
import spring.ecommerce.repository.ProductImageRepository
import spring.ecommerce.service.`interface`.ProductImageService
import kotlin.collections.count


@Service
class ProductImageServiceImpl(
    private val productImageRepository: ProductImageRepository
): ProductImageService {

    override fun createImages(
        productId: Long,
        images: List<ProductImageRequest>
    ): List<ProductImageResponse> {

        if (images.count { it.isPrimary } > 1) {
            throw BadRequestException("Product images can only have a primary value")
        }

        val productImages = images
            .sortedByDescending { it.isPrimary }
            .mapIndexed { index, image ->
                ProductImage(
                    productId = productId,
                    imageUrl = image.imageUrl,
                    displayOrder = index + 1,
                    isPrimary = image.isPrimary
                )
            }

        return productImageRepository
            .saveAll(productImages)
            .map {
                ProductImageResponse(
                    id = it.id,
                    imageUrl = it.imageUrl,
                    displayOrder = it.displayOrder,
                    isPrimary = it.isPrimary
                )
            }
    }

    override fun deleteAllProductImages(productId: Long) {
        val productImages = productImageRepository.findAllByProductId(productId)
        productImageRepository.deleteAll(productImages)
    }

    override fun getProductImages(productId: Long): List<ProductImageResponse> {
        val images = productImageRepository
            .findAllByProductIdOrderByDisplayOrderAsc(productId)
            .map {
                ProductImageResponse(
                    id = it.id,
                    imageUrl = it.imageUrl,
                    displayOrder = it.displayOrder,
                    isPrimary = it.isPrimary
                )
            }
        return images
    }

    override fun getProductImageById(id: Long): Response<ProductImageResponse> {
        val productImage = productImageRepository.findById(id).orElseThrow {
            NotFoundException("Product image not found")
        }

        val response = ProductImageResponse(
            id = productImage.id,
            imageUrl = productImage.imageUrl,
            displayOrder = productImage.displayOrder,
            isPrimary = productImage.isPrimary
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Retrieved a product image"
        )

    }

    @Transactional
    override fun updateProductImages(id: Long, request: UpdatedProductImageRequest): Response<ProductImageResponse> {
        val (imageUrl, isPrimary, displayOrder) = request
        val productImage = productImageRepository.findById(id).orElseThrow {
            NotFoundException("Product images not found")
        }

        imageUrl?.let{
            productImage.imageUrl = it
        }

        isPrimary?.let{ newPrimary ->
            val productId = productImage.productId
                ?: throw NotFoundException("Product image not found")
            val images = productImageRepository.findAllByProductId(productId)

            productImage.isPrimary = newPrimary

            if (images.count { it.isPrimary } > 1) {
                throw BadRequestException("Product images can only have a primary value")
            }
        }

        displayOrder?.let {
            productImage.displayOrder = it
        }

        productImageRepository.save(productImage)

        val response = ProductImageResponse(
            id = productImage.id,
            imageUrl = productImage.imageUrl,
            displayOrder = productImage.displayOrder,
            isPrimary = productImage.isPrimary
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Updated product image"
        )
    }
}