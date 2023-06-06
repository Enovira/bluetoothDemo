package com.enov.bel.core.base

import androidx.lifecycle.ViewModel
import com.enov.bel.AppToast

/**
 * 在ViewModel中快速toast
 */
fun ViewModel.toast(message: String) {
    AppToast.show(message)
}