package co.com.pipearcos221.intercommerceapp.core.domain.error

sealed class AppException : Exception() {

    data object NetworkException : AppException()

    data class HttpException(val code: Int, override val message: String) : AppException()

    data class UnknownException(override val message: String? = null) : AppException()
}
