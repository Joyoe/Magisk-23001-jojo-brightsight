package com.brightsight.magisk.events

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.brightsight.magisk.arch.ActivityExecutor
import com.brightsight.magisk.arch.BaseUIActivity
import com.brightsight.magisk.arch.ViewEvent
import com.brightsight.magisk.utils.TextHolder
import com.brightsight.magisk.utils.asText

class SnackbarEvent constructor(
    private val msg: TextHolder,
    private val length: Int = Snackbar.LENGTH_SHORT,
    private val builder: Snackbar.() -> Unit = {}
) : ViewEvent(), ActivityExecutor {

    constructor(
        @StringRes res: Int,
        length: Int = Snackbar.LENGTH_SHORT,
        builder: Snackbar.() -> Unit = {}
    ) : this(res.asText(), length, builder)

    constructor(
        msg: String,
        length: Int = Snackbar.LENGTH_SHORT,
        builder: Snackbar.() -> Unit = {}
    ) : this(msg.asText(), length, builder)


    private fun snackbar(
        view: View,
        message: String,
        length: Int,
        builder: Snackbar.() -> Unit
    ) = Snackbar.make(view, message, length).apply(builder).show()

    override fun invoke(activity: BaseUIActivity<*, *>) {
        snackbar(activity.snackbarView,
            msg.getText(activity.resources).toString(),
            length, builder)
    }
}
