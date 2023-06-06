package com.enov.bel

import android.content.Context
import androidx.lifecycle.ViewModelStore
import com.enov.bel.core.base.BaseApp
import com.enov.bel.core.base.EventViewModel
import java.lang.ref.WeakReference

class App : BaseApp() {

    companion object {
        lateinit var instance: App
        private lateinit var context: WeakReference<Context>
        fun getContext(): Context {
            return context.get()!!
        }
    }

    val eventViewModel by lazy(lock = this) {
        EventViewModel()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = WeakReference(applicationContext)
    }

    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()
}