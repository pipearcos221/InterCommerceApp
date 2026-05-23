package co.com.pipearcos221.intercommerceapp.core.data.util

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend fun <T> safeCall(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> T
): Result<T> = withContext(dispatcher) {
    try {
        Result.success(block())
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }
}
