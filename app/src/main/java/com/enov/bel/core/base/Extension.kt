package com.enov.bel.core.base

import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.enov.bel.AppToast

/**
 * 在ViewModel中快速toast
 */
fun ViewModel.toast(message: String) {
    AppToast.show(message)
}

fun RelativeLayout.setPreventFastClickListener(listener: PreventFastClickListener){
    setOnClickListener(listener)
}
fun TextView.setPreventFastClickListener(listener: PreventFastClickListener){
    setOnClickListener(listener)
}