package com.enov.bel.core.base

import android.view.View

abstract class PreventFastClickListener: View.OnClickListener {
    private val period: Long = 600
    private var lastClickTime: Long = 0

    override fun onClick(v: View?) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > period) {
            onPreventFastClickListener(v)
            lastClickTime = currentTime
        }
    }

    abstract fun onPreventFastClickListener(v: View?)
}