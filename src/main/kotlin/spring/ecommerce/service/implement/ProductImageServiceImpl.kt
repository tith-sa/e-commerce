package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.ProductImageRequest
import spring.ecommerce.dto.response.ProductImageResponse
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.ProductImage
import spring.ecommerce.repository.ProductImageRepository
import spring.ecommerce.service.`interface`.ProductImageService


@Service
class ProductImageServiceImpl(
    private val productImageRepository: ProductImageRepository
): ProductImageService {

    // add a new single image to existing product
    override fun addNewProductImages(productId: Long, request: ProductImageRequest): Response<ProductImageResponse> {
        val (imageUrl) = request

        val images = productImageRepository.findAllByProductId(productId)


        // create an image
        val productImage = ProductImage(
            productId = productId,
            imageUrl = imageUrl,
            displayOrder = images.size + 1,
        )

        productImageRepository.save(productImage)

        val response = ProductImageResponse(
            id = productImage.id,
            imageUrl = productImage.imageUrl,
            displayOrder = productImage.displayOrder,
        )

        return Response(
            status = HttpStatus.CREATED,
            data = response,
            message = "Product images added successfully"
        )

    }

    override fun deleteAllProductImages(productId: Long) {
        val productImages = productImageRepository.findAllByProductId(productId)
        productImageRepository.deleteAll(productImages)
    }


    // get an image by id
    override fun viewProductImage(id: Long): Response<ProductImageResponse> {
        val productImage = productImageRepository.findById(id).orElseThrow {
            NotFoundException("Product image not found")
        }

        val response = ProductImageResponse(
            id = productImage.id,
            imageUrl = productImage.imageUrl,
            displayOrder = productImage.displayOrder,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Retrieved a product image"
        )

    }

    override fun deleteProductImageById(id: Long): Response<Unit> {
        val productImage = productImageRepository.findById(id).orElseThrow {
            NotFoundException("Product image not found")
        }
        productImageRepository.delete(productImage)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Product image deleted"
        )
    }
}