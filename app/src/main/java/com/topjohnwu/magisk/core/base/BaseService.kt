package com.brightsight.magisk.core.base

import android.app.Service
import android.content.Context
import com.brightsight.magisk.core.wrap

abstract class BaseService : Service() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base.wrap())
    }
}
