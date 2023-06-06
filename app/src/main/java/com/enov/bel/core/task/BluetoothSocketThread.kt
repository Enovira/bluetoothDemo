package com.enov.bel.core.task

import android.annotation.SuppressLint
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import com.enov.bel.core.utils.BluetoothUtil
import com.enov.bel.core.utils.CodeUtil
import java.io.InputStream
import java.util.*

@SuppressLint("MissingPermission")
class BluetoothSocketThread(private val listener: (String) -> Unit) : Thread() {
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val bluetoothName = "bluetoothName"
    private var bluetoothSocket: BluetoothSocket? = null
    private lateinit var bluetoothServerSocket: BluetoothServerSocket
    private var inputStream: InputStream? = null

    init {
        try {
            bluetoothServerSocket =
                BluetoothUtil.instance.startListenToBluetoothSocket(bluetoothName, uuid)
            listener.invoke("成功创建蓝牙ServerSocket")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun run() {
        try {
            listener.invoke("蓝牙ServerSocket开始监听")
            bluetoothSocket = bluetoothServerSocket.accept()
            listener.invoke("蓝牙Socket已连接")
            if (bluetoothSocket!= null) {
                inputStream = bluetoothSocket?.inputStream
                val bytes = ByteArray(1024)
                inputStream?.let {
                    while (!isInterrupted) {
                        sleep(100)
                        if (it.available() > 0) {
                            val count = it.read(bytes)
                            val results = ByteArray(count)
                            System.arraycopy(bytes, 0, results, 0, count)
                            println("接收到消息了 ${CodeUtil.bytes2HexString(results)}")
                            listener.invoke("接收到消息了 ${CodeUtil.bytes2HexString(results)}")
                        }
                    }
                    it.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listener.invoke("蓝牙ServerSocket已结束")
        }
    }

    fun closeSocket() {
        interrupt()
        inputStream?.close()
        bluetoothSocket?.close()
        bluetoothServerSocket.close()
    }

    /**
     * 发送消息
     */
    fun sendMessage(byteArray: ByteArray) {
        bluetoothSocket?.let{
            val outputStream = it.outputStream
            outputStream.write(byteArray)
            listener.invoke("发送消息成功: ${CodeUtil.bytes2HexString(byteArray)}")
            outputStream.flush()
        }
    }
}