package com.brightsight.magisk.ui.log

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.brightsight.magisk.BR
import com.brightsight.magisk.BuildConfig
import com.brightsight.magisk.R
import com.brightsight.magisk.arch.BaseViewModel
import com.brightsight.magisk.arch.diffListOf
import com.brightsight.magisk.arch.itemBindingOf
import com.brightsight.magisk.core.Info
import com.brightsight.magisk.core.utils.MediaStoreUtils
import com.brightsight.magisk.core.utils.MediaStoreUtils.outputStream
import com.brightsight.magisk.data.repository.LogRepository
import com.brightsight.magisk.events.SnackbarEvent
import com.brightsight.magisk.ktx.now
import com.brightsight.magisk.ktx.timeFormatStandard
import com.brightsight.magisk.ktx.toTime
import com.brightsight.magisk.utils.set
import com.brightsight.magisk.view.TextItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogViewModel(
    private val repo: LogRepository
) : BaseViewModel() {

    // --- empty view

    val itemEmpty = TextItem(R.string.log_data_none)
    val itemMagiskEmpty = TextItem(R.string.log_data_magisk_none)

    // --- su log

    val items = diffListOf<LogRvItem>()
    val itemBinding = itemBindingOf<LogRvItem> {
        it.bindExtra(BR.viewModel, this)
    }

    // --- magisk log
    @get:Bindable
    var consoleText = " "
        set(value) = set(value, field, { field = it }, BR.consoleText)

    override fun refresh() = viewModelScope.launch {
        consoleText = repo.fetchMagiskLogs()
        val (suLogs, diff) = withContext(Dispatchers.Default) {
            val suLogs = repo.fetchSuLogs().map { LogRvItem(it) }
            suLogs to items.calculateDiff(suLogs)
        }
        items.firstOrNull()?.isTop = false
        items.lastOrNull()?.isBottom = false
        items.update(suLogs, diff)
        items.firstOrNull()?.isTop = true
        items.lastOrNull()?.isBottom = true
    }

    fun saveMagiskLog() = withExternalRW {
        viewModelScope.launch(Dispatchers.IO) {
            val filename = "magisk_log_%s.log".format(now.toTime(timeFormatStandard))
            val logFile = MediaStoreUtils.getFile(filename, true)
            logFile.uri.outputStream().bufferedWriter().use { file ->
                file.write("---System Properties---\n\n")

                ProcessBuilder("getprop").start()
                    .inputStream.reader().use { it.copyTo(file) }

                file.write("\n---Magisk Logs---\n")
                file.write("${Info.env.magiskVersionString} (${Info.env.magiskVersionCode})\n\n")
                file.write(consoleText)

                file.write("\n---Manager Logs---\n")
                file.write("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n\n")
                ProcessBuilder("logcat", "-d").start()
                    .inputStream.reader().use { it.copyTo(file) }
            }
            SnackbarEvent(logFile.toString()).publish()
        }
    }

    fun clearMagiskLog() = repo.clearMagiskLogs {
        SnackbarEvent(R.string.logs_cleared).publish()
        requestRefresh()
    }

    fun clearLog() = viewModelScope.launch {
        repo.clearLogs()
        SnackbarEvent(R.string.logs_cleared).publish()
        requestRefresh()
    }
}