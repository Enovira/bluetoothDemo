package com.enov.bel.core.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class KeyBoardUtil {
    companion object {
        fun closeSoftKeyBoard(activity: Activity) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }
}