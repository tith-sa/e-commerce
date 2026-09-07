package spring.ecommerce.dto.response

data class PaginationResponse<T>(
    val meta: ResponsePageMeta,
    val contents: List<T>
){
    data class ResponsePageMeta(
        val page: Int,
        val pageSize: Int,
        val totalElements: Long,
        val totalPages: Int,
    )
}
