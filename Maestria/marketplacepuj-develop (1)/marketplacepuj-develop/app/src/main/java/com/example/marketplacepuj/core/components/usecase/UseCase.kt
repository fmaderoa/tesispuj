package com.example.marketplacepuj.core.components.usecase

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

open class UseCase<RESPONSE>(private val abstractUseCase: AbstractUseCase<*, RESPONSE>) {
    var exception: (Throwable) -> Unit = {}

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            exception(throwable)
        }

    private val coroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + coroutineExceptionHandler)

    suspend fun execute(): RESPONSE? {
        return coroutineScope.async(Dispatchers.IO) {
            abstractUseCase.internalExecute()
        }.await()
    }
}

suspend fun <RESPONSE> UseCase<RESPONSE>.onStart(action: () -> Unit): UseCase<RESPONSE> {
    withContext(Dispatchers.Main.immediate) { action() }
    return this
}

fun <RESPONSE> UseCase<RESPONSE>.catch(exception: (Throwable) -> Unit): UseCase<RESPONSE> {
    this.exception = exception
    return this
}

suspend fun <RESPONSE> UseCase<RESPONSE>.result(response: (RESPONSE) -> Unit) {
    val result = execute()
    result?.let {
        withContext(Dispatchers.Main.immediate) { response(it) }
    }
}
