package spring.ecommerce.service.implement

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.ProductRequest
import spring.ecommerce.dto.request.SearchProductRequest
import spring.ecommerce.dto.request.UpdatedProductRequest
import spring.ecommerce.dto.response.CategoryResponse
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.ProductImageResponse
import spring.ecommerce.dto.response.ProductResponse
import spring.ecommerce.dto.response.UserResponse
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Product
import spring.ecommerce.model.ProductImage
import spring.ecommerce.repository.CategoryRepository
import spring.ecommerce.repository.ProductImageRepository
import spring.ecommerce.repository.ProductRepository
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.repository.specification.productSpecification
import spring.ecommerce.service.`interface`.ProductService


@Service
class ProductServiceImpl (
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val productImageRepository: ProductImageRepository,
): ProductService {

    @Transactional
    override fun createProduct(userId: Long, request: ProductRequest): Response<Unit> {
        val (name, price, quantity, description, categoryId, images) = request

        userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow {
            NotFoundException("User not found")
        }

        categoryRepository.findById(categoryId).orElseThrow {
            NotFoundException("Category not found")
        }
        val sortedImages = images.sortedByDescending { it.isPrimary }

        val productImagesPayload = mutableListOf<ProductImage>()

        for ((index, image) in sortedImages.withIndex()) {
            productImagesPayload.add(
                ProductImage(
                    imageUrl = image.imageUrl,
                    displayOrder = index + 1,
                )
            )
        }

        val product = Product(
            name = name,
            price = price,
            description = description,
            quantity = quantity,
            categoryId = categoryId,
            createdById = userId
        )
        productRepository.save(product)

        for (image in productImagesPayload) {
            image.productId = product.id
        }
        productImageRepository.saveAll(productImagesPayload)

        return Response(
            status = HttpStatus.CREATED,
            data = null,
            message = "Product created"
        )
    }

    @Transactional
    override fun updateProduct(id: Long, request: UpdatedProductRequest): Response<Unit> {
        val (name, price, quantity, description, categoryId) = request

        val product = productRepository.findById(id).orElseThrow {
            NotFoundException("Product not found")
        }

        name?.let {
            product.name = it
        }

        price?.let {
            product.price = it
        }

        quantity?.let {
            product.quantity = it
        }

        description?.let {
            product.description = it
        }

        categoryId?.let {
            val category = categoryRepository.findById(it).orElseThrow {
                NotFoundException("Category not found")
            }

            product.categoryId = category.id
        }

        productRepository.save(product)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Product updated"
        )

    }

    override fun viewProduct(id: Long): Response<ProductResponse> {
        val product = productRepository.findById(id).orElseThrow {
            NotFoundException("Product not found")
        }

        val createdBy = product.createdById?.let {
            userRepository.findById(it).orElseThrow {
                NotFoundException("User not found")
            }
        }
        val responseOwner = UserResponse(
            id = createdBy?.id,
            username = createdBy?.username,
            phoneNumber = createdBy?.phoneNumber,
            address = createdBy?.address,
            createdAt = createdBy?.createdAt,
            updatedAt = createdBy?.updatedAt,
        )

        val category = product.categoryId?.let { categoryId ->
            categoryRepository.findById(categoryId).orElse(null)
        }
        val responseCategory = category?.let {
            CategoryResponse(
                id = it.id,
                name = it.name,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
            )
        }

        val images = productImageRepository
            .findAllByProductIdOrderByDisplayOrderAsc(id)
            .map {
                ProductImageResponse(
                    id = it.id,
                    imageUrl = it.imageUrl,
                    displayOrder = it.displayOrder,
                )
            }

        val response = ProductResponse(
            id = product.id,
            name = product.name,
            price = product.price,
            description = product.description,
            quantity = product.quantity,
            createdBy = responseOwner,
            category = responseCategory,
            images = images,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Product updated"
        )
    }

    override fun listProducts(
        search: SearchProductRequest,
        requestPagination: PaginationRequest
    ): Response<PaginationResponse<ProductResponse>> {
        val (page, size) = requestPagination
        val pageable = PageRequest.of(
            page - 1,
            size,
        )
        val specification = productSpecification(search)

        val products = productRepository.findAll(specification, pageable)

        val productId = products.content.map { it.id }.toSet()
        val images = productImageRepository.findAllByProductIdIn(productId)

        val categoryId = products.content.map { it.categoryId }
        val categories = categoryRepository.findByIdIn(categoryId)

        val createdBy = products.content.map { it.createdById }
        val owners = userRepository.findByIdIn(createdBy)

        val mapProduct = products.content.map { product ->

            val category = categories.find { it.id == product.categoryId }
            val responseCategory = category?.let {
                CategoryResponse(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                )
            }

            val owner = owners.find { it.id == product.createdById }
            val responseOwner = UserResponse(
                id = owner?.id,
                username = owner?.username,
                phoneNumber = owner?.phoneNumber,
                address = owner?.address,
                createdAt = owner?.createdAt,
                updatedAt = owner?.updatedAt,
            )

            // image
            val image = images.filter { it.productId == product.id }

            val mapResponseImage = image.map {
                ProductImageResponse(
                    id = it.id,
                    imageUrl = it.imageUrl,
                    displayOrder = it.displayOrder,
                )
            }

            ProductResponse(
                id = product.id,
                name = product.name,
                price = product.price,
                description = product.description,
                category = responseCategory,
                quantity = product.quantity,
                createdBy = responseOwner,
                images = mapResponseImage,
                createdAt = product.createdAt,
                updatedAt = product.updatedAt,
            )
        }

        val pagination = PaginationResponse(
            meta = PaginationResponse.ResponsePageMeta(
                page = page,
                pageSize = size,
                totalElements = products.totalElements,
                totalPages = products.totalPages,
            ),
            contents = mapProduct,
        )

        return Response(
            status = HttpStatus.OK,
            data = pagination,
            message = "Products returned"
        )
    }

}