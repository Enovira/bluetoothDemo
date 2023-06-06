package com.enov.bel.mvvm.vm

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.enov.bel.AppToast
import com.enov.bel.core.base.toast
import com.enov.bel.core.task.BluetoothSocketThread
import com.enov.bel.core.utils.BluetoothUtil
import com.enov.bel.core.utils.CodeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("MissingPermission")
class BluetoothActivityVM(application: Application) : AndroidViewModel(application) {

    private var bluetoothSocketThread: BluetoothSocketThread? = null

    val bondedDevicesLiveData: MutableLiveData<List<BluetoothDevice>> = MutableLiveData()


    fun switchBluetooth() {
        BluetoothUtil.instance.switchBluetooth()
    }

    fun discoverable() {
        BluetoothUtil.instance.discoverable(getApplication())
    }

    fun startDiscovery() {
        if (BluetoothUtil.instance.isDiscovering()) {
            AppToast.show("正在搜索蓝牙设备中")
        } else {
            BluetoothUtil.instance.startDiscoverDevice()
        }
    }

    fun pairBluetoothDevice(bluetoothDevice: BluetoothDevice) {
        when (bluetoothDevice.bondState) {
            BluetoothDevice.BOND_NONE -> BluetoothUtil.instance.createBond(bluetoothDevice)
            BluetoothDevice.BOND_BONDED -> {
                toast("已与该设备配对，无需重复配对")
            }
        }
    }

    fun getBondedDevices(){
        val sets = BluetoothUtil.instance.getBondedBluetoothDevices()
        val list: MutableList<BluetoothDevice> = ArrayList()
        val iterators = sets.iterator()
        iterators.forEach {
            list.add(it)
        }
        bondedDevicesLiveData.postValue(list)
    }

    fun startListenToBluetoothSocket(){
        @Synchronized
        if (bluetoothSocketThread == null) {
            bluetoothSocketThread = BluetoothSocketThread(onStateChangeListener)
            bluetoothSocketThread?.start()
        } else {
            viewModelScope.launch(Dispatchers.Main) {
                toast("BluetoothServerSocketThread is running")
            }
        }
    }

    fun stopListenToBluetoothSocket() {
        if (bluetoothSocketThread != null) {
            bluetoothSocketThread?.closeSocket()
            bluetoothSocketThread = null
        } else {
            viewModelScope.launch(Dispatchers.Main) {
                toast("BluetoothServerSocketThread is interrupted!")
            }
        }
    }

    private val onStateChangeListener: (String) -> Unit = {
        viewModelScope.launch(Dispatchers.Main) {
            println(it)
            toast(it)
        }
    }

    fun sendMessage(byteArray: ByteArray) {
        bluetoothSocketThread?.sendMessage(byteArray)
    }
}