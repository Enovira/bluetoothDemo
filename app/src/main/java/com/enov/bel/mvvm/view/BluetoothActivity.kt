package com.enov.bel.mvvm.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.viewModelScope
import com.enov.bel.App
import com.enov.bel.AppToast
import com.enov.bel.R
import com.enov.bel.core.base.BaseActivity
import com.enov.bel.core.global.Cons
import com.enov.bel.core.utils.BluetoothUtil
import com.enov.bel.core.utils.CodeUtil
import com.enov.bel.core.utils.DialogFactory
import com.enov.bel.core.utils.KeyBoardUtil
import com.enov.bel.databinding.ActivityBluetoothBinding
import com.enov.bel.mvvm.view.adapter.BluetoothDevicesAdapter
import com.enov.bel.mvvm.vm.BluetoothActivityVM
import com.kongzue.dialogx.dialogs.MessageDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@SuppressLint("MissingPermission")
class BluetoothActivity : BaseActivity<BluetoothActivityVM, ActivityBluetoothBinding>() {

    private lateinit var foundDeviceAdapter: BluetoothDevicesAdapter
    private lateinit var bondedDeviceAdapter: BluetoothDevicesAdapter
    private val foundBluetoothDeviceList: MutableList<BluetoothDevice> = ArrayList()
    private val bondedBluetoothDeviceList: MutableList<BluetoothDevice> = ArrayList()

    private var bluetoothDiscoverableExpiredTime: Long = 0
    private val DURATION_TIME: Long = 300 * 1000L // 默认120s,最大300s

    private var bluetoothSocket: BluetoothSocket? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_bluetooth
    }

    override fun initView() {
        binding.viewModel = viewModel
        initRecyclerView()
        initListener()
        initObserver()
    }

    private fun initRecyclerView() {
        foundDeviceAdapter = BluetoothDevicesAdapter()
        bondedDeviceAdapter = BluetoothDevicesAdapter()
        binding.recyclerView.adapter = foundDeviceAdapter
        binding.recyclerView2.adapter = bondedDeviceAdapter
        foundDeviceAdapter.setNewInstance(foundBluetoothDeviceList)
        bondedDeviceAdapter.setNewInstance(bondedBluetoothDeviceList)
        foundDeviceAdapter.setEmptyView(R.layout.empty_recycle_view)
        bondedDeviceAdapter.setEmptyView(R.layout.empty_recycle_view)
        foundDeviceAdapter.setOnItemClickListener { _, _, position ->
            DialogFactory.showMessageDialog { messageDialog: MessageDialog, _: View ->
                viewModel.pairBluetoothDevice(foundBluetoothDeviceList[position])
                messageDialog.dismiss()
                false
            }
        }
        bondedDeviceAdapter.setOnItemClickListener { _, _, position ->
            MessageDialog.show("提示", "是否连接该设备", "确认连接", "取消").setOkButton { _, _ ->
                Thread {
                    if (bluetoothSocket == null) {
                        bluetoothSocket = BluetoothUtil.instance.createRfcommSocketToServiceRecord(
                            bondedBluetoothDeviceList[position],
                            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                        )
                    }
                    try {
                        DialogFactory.showWaitDialog("正在连接蓝牙中,请稍后...")
                        runOnUiThread {
                            binding.drawerLayout.closeDrawer(GravityCompat.END)
                        }
                        bluetoothSocket?.connect()
                        sendBroadcast(Intent(Cons.intent.CONNECT_TO_BLUETOOTH))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        runOnUiThread {
                            AppToast.show("连接蓝牙失败")
                            DialogFactory.dismissWaitDialog()
                        }
                    }
                }.start()
                false
            }
        }

    }

    private fun initListener() {
        binding.openDrawerLayout.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.discoverable.setOnClickListener {
            if (bluetoothDiscoverableExpiredTime < System.currentTimeMillis()) {
                viewModel.discoverable()
            } else {
                Toast.makeText(this, "蓝牙已处于可被搜索状态", Toast.LENGTH_SHORT).show()
            }
        }
        binding.messageContent.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                KeyBoardUtil.closeSoftKeyBoard(this@BluetoothActivity)
            }
        }
        binding.sendMessage.setOnClickListener {
            val content = binding.messageContent.text.trim().toString()
            if (content == "") {
                Toast.makeText(
                    this@BluetoothActivity,
                    binding.messageContent.hint,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                val outputStream = bluetoothSocket?.outputStream
                val bytes = CodeUtil.toBytes(content.replace(" ", ""))
                outputStream?.write(bytes)
                println("发送消息成功 ${CodeUtil.bytes2HexString(bytes)}")
                withContext(Dispatchers.Main) {
                    AppToast.show("发送消息成功 ${CodeUtil.bytes2HexString(bytes)}")
                }
                outputStream?.flush()
            }
        }
        binding.sendMessageFromMain.setOnClickListener {
            val content = binding.messageContent.text.trim().toString()
            if (content == "") {
                Toast.makeText(
                    this@BluetoothActivity,
                    binding.messageContent.hint,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val str =
                // JH6000
//                "68 00 A2 68 30 00 00 1A 0A 81 04 03 07 DF 04 11 00 01 00 00 00 00 00 03 F6 00 01 00 00 00 1E 00 00 64 00 03 3F FA E1 48 00 00 6B 00 03 42 62 CC CD 00 15 09 0F 0A 1B 19 00 68 30 00 00 1A 0A 81 04 03 07 DF 04 11 00 01 00 00 00 01 00 03 F6 00 01 00 00 00 1E 00 00 64 00 03 3F 83 D7 0A 00 00 6B 00 03 42 5D 99 9A 00 15 09 0F 0A 1B 19 00 68 30 00 00 1A 0A 81 04 03 07 DF 04 11 00 01 00 00 00 02 00 03 F6 00 01 00 00 00 1E 00 00 64 00 03 3F 91 EB 85 00 00 6B 00 03 42 5C CC CD 00 15 09 0F 0A 1B 19 00 56 16"
            // 回路电阻测试仪 JYL_100B
//            "7E 35 35 31 34 48 30 31 30 30 2E 39 34 39 39 2E 36 20 75 5C 0D"
                //SF6气体检漏仪 JHLK_100
                "65 02 03 00 56 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 02 00 00 00 00 00 00 00 03 41 20 00 00 42 C8 00 00 2E 41 20 00 00 41 20 00 00 40 D8 A6 4E 00 E1 C5 56"
                //HVM-5000
//                "42 45 47 50 00 00 00 03 00 41 00 00 00 E6 07 07 19 0C 30 1A 00 F0 1D 45 05 EF 6A DC 42 E7 9F DC 42 00 00 00 00 1B 9A C2 80 3F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0000 00 00 00 00 00 00 00 00 00 00 00 00 00 0000 00 00 D3 29"
//            viewModel.sendMessage(CodeUtil.toBytes(content))
            viewModel.sendMessage(str.replace(" ", ""))
        }
    }

    private fun initObserver() {
        viewModel.bondedDevicesLiveData.observe(this) {
            bondedBluetoothDeviceList.clear()
            bondedBluetoothDeviceList.addAll(it)
            bondedDeviceAdapter.notifyDataSetChanged()
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        App.instance.eventViewModel.bluetoothStateLiveData.observe(this) {
            if (it) {
                Toast.makeText(App.getContext(), "蓝牙已开启", Toast.LENGTH_SHORT).show()
            } else {
                bluetoothDiscoverableExpiredTime = 0
                Toast.makeText(App.getContext(), "蓝牙已关闭", Toast.LENGTH_SHORT).show()
            }
        }
        App.instance.eventViewModel.bluetoothFoundDeviceLiveData.observe(this) {
            foundBluetoothDeviceList.add(it)
            foundDeviceAdapter.notifyItemInserted(foundBluetoothDeviceList.size)
        }
        App.instance.eventViewModel.bluetoothIsDiscoveringLiveData.observe(this) {
            if (it) {
                AppToast.show("开始搜索蓝牙设备")
                val count = foundBluetoothDeviceList.size
                foundBluetoothDeviceList.clear()
                foundDeviceAdapter.notifyItemRangeRemoved(0, count)
                binding.drawerLayout.openDrawer(GravityCompat.START)
            } else {
                AppToast.show("搜索蓝牙设备完成")
            }
        }
        App.instance.eventViewModel.bluetoothDiscoverable.observe(this) {
            if (it) {
                AppToast.show("蓝牙现在可被搜索")
                bluetoothDiscoverableExpiredTime = System.currentTimeMillis() + DURATION_TIME
            }
        }
        App.instance.eventViewModel.bluetoothSocketConnectLiveData.observe(this) {
            AppToast.show("蓝牙Socket已连接")
            try {
                Thread {
                    bluetoothSocket?.let {
                        val inputStream = it.inputStream
                        val bytes = ByteArray(1024)
                        println("监听输入流")
                        while (!Thread.interrupted()) {
                            Thread.sleep(100)
                            if (inputStream.available() > 0) {
                                val count = inputStream.read(bytes)
                                val result = ByteArray(count)
                                System.arraycopy(bytes, 0, result, 0, count)
                                println("接收到消息: ${CodeUtil.bytes2HexString(result)}")
                                runOnUiThread {
                                    AppToast.show("接收到消息: ${CodeUtil.bytes2HexString(result)}")
                                }
                            }
                        }
                    }
                }.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            DialogFactory.dismissWaitDialog()
        }
    }
}