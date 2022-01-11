package com.brightsight.magisk.core

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.*
import com.brightsight.magisk.BuildConfig
import com.brightsight.magisk.di.ServiceLocator
import com.brightsight.magisk.view.Notifications
import java.util.concurrent.TimeUnit

class UpdateCheckService(context: Context, workerParams: WorkerParameters)
    : CoroutineWorker(context, workerParams) {

    private val svc get() = ServiceLocator.networkService

    override suspend fun doWork(): Result {
        return svc.fetchUpdate()?.run {
            if (Info.env.isActive && BuildConfig.VERSION_CODE < magisk.versionCode)
                Notifications.managerUpdate(applicationContext)
            Result.success()
        } ?: Result.failure()
    }

    companion object {
        @SuppressLint("NewApi")
        fun schedule(context: Context) {
            if (Config.checkUpdate) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresDeviceIdle(true)
                    .build()
                val request = PeriodicWorkRequestBuilder<UpdateCheckService>(12, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .build()
                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    Const.ID.CHECK_MAGISK_UPDATE_WORKER_ID,
                    ExistingPeriodicWorkPolicy.REPLACE, request)
            } else {
                WorkManager.getInstance(context)
                    .cancelUniqueWork(Const.ID.CHECK_MAGISK_UPDATE_WORKER_ID)
            }
        }
    }
}
