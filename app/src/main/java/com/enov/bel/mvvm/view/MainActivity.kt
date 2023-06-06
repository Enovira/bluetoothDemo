package com.enov.bel.mvvm.view

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import com.enov.bel.R
import com.enov.bel.core.base.BaseActivity
import com.enov.bel.core.global.Cons
import com.enov.bel.core.receiver.BluetoothBroadcastReceiver
import com.enov.bel.databinding.ActivityMainBinding
import com.enov.bel.mvvm.view.adapter.PV2Adapter
import com.enov.bel.mvvm.vm.MainViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private lateinit var pv2Adapter: PV2Adapter
    private val receiver: BluetoothBroadcastReceiver by lazy(lock = this) {
        BluetoothBroadcastReceiver()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        pv2Adapter = PV2Adapter(this)
        binding.pageView2.adapter = pv2Adapter
        binding.pageView2.currentItem = 0
        initListener()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            launcher.launch(arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        intentFilter.addAction(Cons.intent.CONNECT_TO_BLUETOOTH)
        registerReceiver(receiver, intentFilter)
    }

    private fun initListener() {
        binding.navigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.first -> {
                    binding.pageView2.currentItem = 0
                }
                R.id.second -> {
                    binding.pageView2.currentItem = 1
                }
                R.id.third -> {
                    binding.pageView2.currentItem = 2
                }
            }
            true
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        for (item in it) {
            when(item.key) {
                Manifest.permission.BLUETOOTH_CONNECT -> {
                    println("获取蓝牙权限成功")
                }
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    println("申请位置权限成功")
                }
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}