package spring.ecommerce.service.implement

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.ProductRequest
import spring.ecommerce.dto.request.SearchProductRequest
import spring.ecommerce.dto.request.UpdatedProductRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.ProductResponse
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Product
import spring.ecommerce.repository.CategoryRepository
import spring.ecommerce.repository.ProductImageRepository
import spring.ecommerce.repository.ProductRepository
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.service.`interface`.ProductImageService
import spring.ecommerce.service.`interface`.ProductService


@Service
class ProductServiceImpl (
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val productImageService: ProductImageService,
): ProductService {

    @Transactional
    override fun createProduct(userId: Long, request: ProductRequest): Response<ProductResponse> {
        val (name, price, quantity, description, categoryName, images) = request

        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("User not found")
        }

        if ( user.isDeleted){
            throw NotFoundException("User not found")
        }

        val category = categoryRepository.findByName(categoryName).orElseThrow{
            NotFoundException("Category not found")
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

        val productId = product.id
            ?: throw NotFoundException("Product not found")

        val productImage = productImageService.createImages(
            productId = productId,
            images = images
        )

        val response = ProductResponse(
            id = product.id,
            name = product.name,
            price = product.price,
            description = product.description,
            categoryName = category.name,
            createdBy = user.username,
            images = productImage
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
                NotFoundException("Category not found")
            }

            val userId = product.createdBy
                ?: throw NotFoundException("User not found")
            val user = userRepository.findById(userId).orElseThrow {
                NotFoundException("User not found")
            }

            val productId = product.id
            ?: throw NotFoundException("Product ID not found")
            val images = productImageService.getProductImages(productId)

            ProductResponse(
                id = product.id,
                name = product.name,
                price = product.price,
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

    @Transactional
    override fun updateProduct(id: Long, request: UpdatedProductRequest): Response<ProductResponse> {
        val (name, price, quantity, description, categoryName) = request

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

        categoryName?.let {
            val category = categoryRepository.findByName(it).orElseThrow {
                NotFoundException("Category not found")
            }

            product.categoryId = category.id
        }

        productRepository.save(product)

        val createById = product.createdBy
            ?: throw NotFoundException("Created product not found")
        val owner = userRepository.findById(createById).orElseThrow {
            NotFoundException("User not found")
        }

        val categoryId = product.categoryId
            ?: throw NotFoundException("Category not found")
        val category = categoryRepository.findById(categoryId).orElseThrow{
            NotFoundException("Category not found")
        }

        val productId = product.id
            ?: throw NotFoundException("Product not found")
        val images = productImageService.getProductImages(productId)

        val response = ProductResponse(
            id = product.id,
            name = product.name,
            price = product.price,
            description = product.description,
            createdBy = owner.username,
            categoryName = category.name,
            images = images,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Product updated"
        )

    }

    @Transactional
    override fun deleteProduct(id: Long): Response<Unit> {
        val product = productRepository.findById(id).orElseThrow {
            NotFoundException("Product not found")
        }
        productRepository.delete(product)

        val productId = product.id
            ?: throw NotFoundException("Product not found")

        productImageService.deleteAllProductImages(productId)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Products deleted"
        )

    }

    override fun getProductById(id: Long): Response<ProductResponse> {
        val product = productRepository.findById(id).orElseThrow{
            NotFoundException("Product not found")
        }

        val createById = product.createdBy
            ?: throw NotFoundException("Created product not found")
        val owner = userRepository.findById(createById).orElseThrow {
            NotFoundException("User not found")
        }

        val categoryId = product.categoryId
            ?: throw NotFoundException("Category not found")
        val category = categoryRepository.findById(categoryId).orElseThrow{
            NotFoundException("Category not found")
        }

        val productId = product.id
            ?: throw NotFoundException("Product not found")
        val images = productImageService.getProductImages(productId)

        val response = ProductResponse(
            id = product.id,
            name = product.name,
            price = product.price,
            description = product.description,
            createdBy = owner.username,
            categoryName = category.name,
            images = images,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Product updated"
        )
    }

    override fun searchProducts(
        request: SearchProductRequest,
        requestPagination: PaginationRequest
    ): Response<PaginationResponse<ProductResponse>>{
        TODO()
    }

}