package com.enov.bel

import android.widget.Toast

class AppToast {
    companion object {
        fun show(string: String) {
            Toast.makeText(App.getContext(), string, Toast.LENGTH_SHORT).show()
        }
    }
}