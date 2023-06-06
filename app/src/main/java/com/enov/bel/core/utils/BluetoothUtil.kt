package com.enov.bel.core.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import com.enov.bel.AppToast
import java.util.*


@SuppressLint("MissingPermission")
class BluetoothUtil {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    companion object {
        val instance: BluetoothUtil by lazy {
            BluetoothUtil()
        }
    }

    /**
     * 开启/关闭 蓝牙
     */
    fun switchBluetooth() {
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        } else {
            bluetoothAdapter.enable()
        }
    }

    /**
     * 设置蓝牙可被搜索 默认120秒 最大300秒
     */
    fun discoverable(context: Context) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        context.startActivity(intent)
    }

    /**
     * 开始搜索其他蓝牙设备
     */
    fun startDiscoverDevice() {
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.startDiscovery()
        } else {
            AppToast.show("蓝牙未打开，请先开启蓝牙")
        }
    }

    /**
     * 是否正在搜索其他蓝牙设备中
     */
    fun isDiscovering(): Boolean {
        return bluetoothAdapter.isDiscovering
    }

    /**
     * 尝试与蓝牙设备进行连接
     */
    fun createBond(bluetoothDevice: BluetoothDevice) {
        bluetoothDevice.createBond()
    }

    /**
     * 获取已配对的蓝牙设备列表
     */
    fun getBondedBluetoothDevices(): Set<BluetoothDevice> {
        return bluetoothAdapter.bondedDevices
    }

//    fun getDeclaredBluetoothAdapter(): BluetoothAdapter {
//        return this.bluetoothAdapter
//    }

    /**
     * 开始监听蓝牙Socket服务
     */
    fun startListenToBluetoothSocket(name: String, uuid: UUID): BluetoothServerSocket {
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        return bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(name, uuid)
    }

    fun createRfcommSocketToServiceRecord(device: BluetoothDevice, uuid: UUID): BluetoothSocket {
        return device.createRfcommSocketToServiceRecord(uuid)
    }

}