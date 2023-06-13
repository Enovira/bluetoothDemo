package com.enov.bel.mvvm.view.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.enov.bel.R

class BluetoothDevicesAdapter(layoutResId: Int = R.layout.adapter_bluetooth_device_item) : BaseQuickAdapter<BluetoothDevice, BaseViewHolder>(layoutResId) {

    @SuppressLint("MissingPermission")
    override fun convert(holder: BaseViewHolder, item: BluetoothDevice) {
        holder.setText(R.id.name, item.name)
            .setText(R.id.address, item.address)
    }
}