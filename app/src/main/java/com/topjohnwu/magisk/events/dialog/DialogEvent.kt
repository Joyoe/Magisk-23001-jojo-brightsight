package com.brightsight.magisk.events.dialog

import com.brightsight.magisk.arch.ActivityExecutor
import com.brightsight.magisk.arch.BaseUIActivity
import com.brightsight.magisk.arch.ViewEvent
import com.brightsight.magisk.view.MagiskDialog

abstract class DialogEvent : ViewEvent(), ActivityExecutor {

    protected lateinit var dialog: MagiskDialog

    override fun invoke(activity: BaseUIActivity<*, *>) {
        dialog = MagiskDialog(activity)
            .apply { setOwnerActivity(activity) }
            .apply(this::build).reveal()
    }

    abstract fun build(dialog: MagiskDialog)

}

typealias GenericDialogListener = () -> Unit
