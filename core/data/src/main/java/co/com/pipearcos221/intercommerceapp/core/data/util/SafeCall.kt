package co.com.pipearcos221.intercommerceapp.core.data.util

import co.com.pipearcos221.intercommerceapp.core.domain.error.AppException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> T
): Result<T> = withContext(dispatcher) {
    try {
        Result.success(block())
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(mapToAppException(e))
    }
}

fun mapToAppException(e: Throwable): AppException {
    return when (e) {
        is IOException -> AppException.NetworkException
        is HttpException -> AppException.HttpException(
            code = e.code(),
            message = e.message() ?: "HTTP Error"
        )
        is AppException -> e
        else -> AppException.UnknownException(e.message)
    }
}
