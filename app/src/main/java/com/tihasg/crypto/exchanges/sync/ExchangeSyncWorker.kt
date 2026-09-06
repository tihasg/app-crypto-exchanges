package com.tihasg.crypto.exchanges.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cryptoexchanges.domain.model.DomainResult
import com.cryptoexchanges.domain.usecase.GetExchangesUseCase

class ExchangeSyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val getExchangesUseCase: GetExchangesUseCase,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = when (getExchangesUseCase()) {
        is DomainResult.Success -> Result.success()
        is DomainResult.Error -> Result.retry()
    }

    companion object {
        const val UNIQUE_WORK_NAME = "exchange_sync"
    }
}
