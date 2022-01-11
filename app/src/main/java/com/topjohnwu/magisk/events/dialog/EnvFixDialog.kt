package com.brightsight.magisk.events.dialog

import androidx.lifecycle.lifecycleScope
import com.brightsight.magisk.R
import com.brightsight.magisk.core.base.BaseActivity
import com.brightsight.magisk.core.tasks.MagiskInstaller
import com.brightsight.magisk.view.MagiskDialog
import kotlinx.coroutines.launch

class EnvFixDialog : DialogEvent() {

    override fun build(dialog: MagiskDialog) = dialog
        .applyTitle(R.string.env_fix_title)
        .applyMessage(R.string.env_fix_msg)
        .applyButton(MagiskDialog.ButtonType.POSITIVE) {
            titleRes = android.R.string.ok
            preventDismiss = true
            onClick {
                dialog.applyTitle(R.string.setup_title)
                    .applyMessage(R.string.setup_msg)
                    .resetButtons()
                    .cancellable(false)
                (dialog.ownerActivity as BaseActivity).lifecycleScope.launch {
                    MagiskInstaller.FixEnv {
                        dialog.dismiss()
                    }.exec()
                }
            }
        }
        .applyButton(MagiskDialog.ButtonType.NEGATIVE) {
            titleRes = android.R.string.cancel
        }
        .let { }

    companion object {
        const val DISMISS = "com.brightsight.magisk.ENV_DONE"
    }
}
