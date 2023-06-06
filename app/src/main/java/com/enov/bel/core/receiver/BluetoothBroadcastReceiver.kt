package com.enov.bel.core.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.enov.bel.App
import com.enov.bel.AppToast
import com.enov.bel.core.global.Cons

/**
 * 监听蓝牙广播进行相应处理
 */
class BluetoothBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                    BluetoothAdapter.STATE_ON -> {
                        App.instance.eventViewModel.bluetoothStateLiveData.postValue(true)
                    }
                    BluetoothAdapter.STATE_OFF -> {
                        App.instance.eventViewModel.bluetoothStateLiveData.postValue(false)
                    }
                }
            }
            BluetoothDevice.ACTION_FOUND -> {
                val devices: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                devices?.let {
                    if (it.name != null) {
                        App.instance.eventViewModel.bluetoothFoundDeviceLiveData.postValue(it)
                        println("蓝牙名称：${it.name} MAC: ${it.address}")
                    }
                }
            }
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                App.instance.eventViewModel.bluetoothIsDiscoveringLiveData.postValue(true)
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                App.instance.eventViewModel.bluetoothIsDiscoveringLiveData.postValue(false)
            }
            BluetoothAdapter.ACTION_SCAN_MODE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1)
                if (state == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    App.instance.eventViewModel.bluetoothDiscoverable.postValue(true)
                }
            }
            BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                when(intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)) {
                    BluetoothDevice.BOND_NONE -> {
                        AppToast.show("已取消与该设备的配对")
                    }
                    BluetoothDevice.BOND_BONDING -> {
                        AppToast.show("正在与设备配对")
                    }
                    BluetoothDevice.BOND_BONDED -> {
                        AppToast.show("与设备配对成功")
                    }
                }
            }
            Cons.intent.CONNECT_TO_BLUETOOTH -> {
                App.instance.eventViewModel.bluetoothSocketConnectLiveData.postValue(true)
            }
        }
    }
}