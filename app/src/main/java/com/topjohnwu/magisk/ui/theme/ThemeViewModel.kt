package com.brightsight.magisk.ui.theme

import com.brightsight.magisk.arch.BaseViewModel
import com.brightsight.magisk.events.RecreateEvent
import com.brightsight.magisk.events.dialog.DarkThemeDialog
import com.brightsight.magisk.view.TappableHeadlineItem

class ThemeViewModel : BaseViewModel(), TappableHeadlineItem.Listener {

    val themeHeadline = TappableHeadlineItem.ThemeMode

    override fun onItemPressed(item: TappableHeadlineItem) = when (item) {
        is TappableHeadlineItem.ThemeMode -> darkModePressed()
        else -> Unit
    }

    fun saveTheme(theme: Theme) {
        theme.select()
        RecreateEvent().publish()
    }

    private fun darkModePressed() = DarkThemeDialog().publish()

}
