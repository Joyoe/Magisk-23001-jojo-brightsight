package com.brightsight.magisk.ui.log

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import com.brightsight.magisk.R
import com.brightsight.magisk.arch.BaseUIFragment
import com.brightsight.magisk.databinding.FragmentLogMd2Binding
import com.brightsight.magisk.di.viewModel
import com.brightsight.magisk.ktx.addSimpleItemDecoration
import com.brightsight.magisk.ktx.addVerticalPadding
import com.brightsight.magisk.ktx.fixEdgeEffect
import com.brightsight.magisk.ui.MainActivity
import com.brightsight.magisk.utils.MotionRevealHelper

class LogFragment : BaseUIFragment<LogViewModel, FragmentLogMd2Binding>() {

    override val layoutRes = R.layout.fragment_log_md2
    override val viewModel by viewModel<LogViewModel>()

    private var actionSave: MenuItem? = null
    private var isMagiskLogVisible
        get() = binding.logFilter.isVisible
        set(value) {
            MotionRevealHelper.withViews(binding.logFilter, binding.logFilterToggle, value)
            actionSave?.isVisible = !value
            with(activity as MainActivity) {
                invalidateToolbar()
                requestNavigationHidden(value)
                setDisplayHomeAsUpEnabled(value)
            }
        }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        activity.title = resources.getString(R.string.logs)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logFilterToggle.setOnClickListener {
            isMagiskLogVisible = true
        }

        val resource = requireContext().resources
        val l_50 = resource.getDimensionPixelSize(R.dimen.l_50)
        val l1 = resource.getDimensionPixelSize(R.dimen.l1)
        binding.logFilterSuperuser.logSuperuser.addVerticalPadding(
            0,
            l1
        )
        binding.logFilterSuperuser.logSuperuser.addSimpleItemDecoration(
            left = l1,
            top = l_50,
            right = l1,
            bottom = l_50,
        )
        binding.logFilterSuperuser.logSuperuser.fixEdgeEffect()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_log_md2, menu)
        actionSave = menu.findItem(R.id.action_save)?.also {
            it.isVisible = !isMagiskLogVisible
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> viewModel.saveMagiskLog()
            R.id.action_clear ->
                if (!isMagiskLogVisible) viewModel.clearMagiskLog()
                else viewModel.clearLog()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onPreBind(binding: FragmentLogMd2Binding) = Unit

    override fun onBackPressed(): Boolean {
        if (binding.logFilter.isVisible) {
            isMagiskLogVisible = false
            return true
        }
        return super.onBackPressed()
    }

}
