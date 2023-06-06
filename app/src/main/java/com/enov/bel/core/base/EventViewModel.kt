package com.enov.bel.core.base

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 全局ViewModel，用于事件通知
 */
class EventViewModel: ViewModel() {
    /**
     * 蓝牙状态
     */
    val bluetoothStateLiveData = MutableLiveData<Boolean>()

    /**
     * 蓝牙广播 搜索到的蓝牙设备
     */
    val bluetoothFoundDeviceLiveData = MutableLiveData<BluetoothDevice>()

    /**
     * 蓝牙广播 蓝牙可被搜索
     */
    val bluetoothDiscoverable = MutableLiveData<Boolean>()

    /**
     * 蓝牙广播 是否开始搜索其他蓝牙设备状态
     */
    val bluetoothIsDiscoveringLiveData = MutableLiveData<Boolean>()
    /**
     * 自定义蓝牙广播
     */
    val bluetoothSocketConnectLiveData = MutableLiveData<Boolean>()
}