package com.example.androidcomponentsamples.components.WorkManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import java.net.UnknownHostException

class UpdateDbWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            updateLocalDb()
            println("sucess")
            Result.success()

        } catch (e: Exception) {
            if (e is UnknownHostException) {
                println("retry")
                Result.retry()
            } else {
                println("failure")
                Result.failure(Data.Builder().putString("error", e.message.toString()).build())
            }
        }

    }

    private suspend fun updateLocalDb() {
        delay(3000)
        println("updated")
    }
}