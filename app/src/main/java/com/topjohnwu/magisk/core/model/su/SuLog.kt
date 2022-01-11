package com.brightsight.magisk.core.model.su

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.brightsight.magisk.core.model.su.SuPolicy.Companion.ALLOW
import com.brightsight.magisk.ktx.now
import com.brightsight.magisk.ktx.timeFormatTime
import com.brightsight.magisk.ktx.toTime

@Entity(tableName = "logs")
data class SuLog(
    val fromUid: Int,
    val toUid: Int,
    val fromPid: Int,
    val packageName: String,
    val appName: String,
    val command: String,
    val action: Boolean,
    val time: Long = -1
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    @Ignore val timeString = time.toTime(timeFormatTime)
}

fun SuPolicy.toLog(
    toUid: Int,
    fromPid: Int,
    command: String
) = SuLog(uid, toUid, fromPid, packageName, appName, command, policy == ALLOW, now)
