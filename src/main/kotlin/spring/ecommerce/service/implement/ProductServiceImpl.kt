package spring.ecommerce.service.implement

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.ProductRequest
import spring.ecommerce.dto.request.ProductUpdateRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.ProductResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Product
import spring.ecommerce.model.ProductImage
import spring.ecommerce.repository.CategoryRepository
import spring.ecommerce.repository.ProductImageRepository
import spring.ecommerce.repository.ProductRepository
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.service.`interface`.ProductService


@Service
class ProductServiceImpl (
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
): ProductService {

    @Transactional
    override fun createProduct(userId: Long, request: ProductRequest): Response<ProductResponse> {
        val (name, price, quantity, description, categoryName, images) = request

        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("User not found")
        }

        if ( user.isDeleted == true){
            throw NotFoundException("User not found")
        }

        val category = categoryRepository.findByName(categoryName).orElseThrow{
            throw NotFoundException("Category not found")
        }

        val product = Product(
            name = name,
            price = price,
            description = description,
            quantity = quantity,
            categoryId = category.id,
            createdBy = user.id
        )
        productRepository.save(product)

        if (images.count { it.isPrimary } > 1) {
            throw BadRequestException(
                "Only one product image can be primary."
            )
        }
        val sortedImages = images.sortedByDescending { it.isPrimary }
        val productImage = sortedImages.mapIndexed { index, image ->
            ProductImage(
                productId = product.id,
                imageUrl = image.imageUrl,
                displayOrder = index + 1,
                isPrimary = image.isPrimary
            )
        }.toMutableList()

        val saveImages =productImageRepository.saveAll(productImage)

        val imageResponses = saveImages
            .map {
                ProductResponse.Image(
                    id = it.id,
                    imageUrl = it.imageUrl,
                    displayOrder = it.displayOrder,
                    isPrimary = it.isPrimary
                )
            }
            .toMutableList()

        val response = ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            categoryName = category.name,
            createdBy = user.username,
            images = imageResponses
        )

        return Response(
            status = HttpStatus.CREATED,
            data = response,
            message = "Product created"
        )
    }

    override fun getAllProducts(request: PaginationRequest): Response<PaginationResponse<ProductResponse>> {
        val (page, size) = request

        val pageable = PageRequest.of(
            page - 1,
            size
        )

        val products = productRepository.findAllByOrderByCreatedAtDesc(pageable)

        val mapProduct = products.content.map { product ->
            val categoryId = product.categoryId
                ?: throw NotFoundException("Category not found")
            val category = categoryRepository.findById(categoryId).orElseThrow {
                throw NotFoundException("Category not found")
            }

            val userId = product.createdBy
                ?: throw NotFoundException("User not found")
            val user = userRepository.findById(userId).orElseThrow {
                throw NotFoundException("User not found")
            }

            val productId = product.id
            ?: throw NotFoundException("Product ID not found")

            val images = productImageRepository
                .findAllByProductIdOrderByDisplayOrderAsc(productId)
                .map {
                    ProductResponse.Image(
                        id = it.id,
                        imageUrl = it.imageUrl,
                        displayOrder = it.displayOrder,
                        isPrimary = it.isPrimary
                    )
                }
                .toMutableList()

            ProductResponse(
                id = product.id,
                name = product.name,
                description = product.description,
                categoryName = category.name,
                createdBy = user.username,
                images = images,
            )

        }
        val pagination = PaginationResponse(
            meta = PaginationResponse.ResponsePageMeta(
                page = page,
                pageSize = size,
                totalElements = products.totalElements,
                totalPages = products.totalPages,
            ),
            contents = mapProduct
        )

        return Response(
            status = HttpStatus.OK,
            data = pagination,
            message = "Products returned"
        )
    }

    override fun updateProduct(request: ProductUpdateRequest): Response<ProductResponse> {
        TODO()
    }
}