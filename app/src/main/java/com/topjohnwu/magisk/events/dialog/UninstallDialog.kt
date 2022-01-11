package com.brightsight.magisk.events.dialog

import android.app.ProgressDialog
import android.widget.Toast
import com.brightsight.magisk.R
import com.brightsight.magisk.arch.BaseUIActivity
import com.brightsight.magisk.ui.flash.FlashFragment
import com.brightsight.magisk.utils.Utils
import com.brightsight.magisk.view.MagiskDialog
import com.topjohnwu.superuser.Shell

class UninstallDialog : DialogEvent() {

    override fun build(dialog: MagiskDialog) {
        dialog.applyTitle(R.string.uninstall_magisk_title)
            .applyMessage(R.string.uninstall_magisk_msg)
            .applyButton(MagiskDialog.ButtonType.POSITIVE) {
                titleRes = R.string.restore_img
                onClick { restore() }
            }
            .applyButton(MagiskDialog.ButtonType.NEGATIVE) {
                titleRes = R.string.complete_uninstall
                onClick { completeUninstall() }
            }
    }

    @Suppress("DEPRECATION")
    private fun restore() {
        val dialog = ProgressDialog(dialog.context).apply {
            setMessage(dialog.context.getString(R.string.restore_img_msg))
            show()
        }

        Shell.jojo("restore_imgs").submit { result ->
            dialog.dismiss()
            if (result.isSuccess) {
                Utils.toast(R.string.restore_done, Toast.LENGTH_SHORT)
            } else {
                Utils.toast(R.string.restore_fail, Toast.LENGTH_LONG)
            }
        }
    }

    private fun completeUninstall() {
        (dialog.ownerActivity as? BaseUIActivity<*, *>)
                ?.navigation?.navigate(FlashFragment.uninstall())
    }

}
