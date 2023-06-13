package com.enov.bel.core.utils

import android.annotation.SuppressLint
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener

class DialogFactory {

    companion object {
        private var waitDialog: WaitDialog? = null
        fun showWaitDialog(message: String? = "正在处理中,请稍后......") {
            waitDialog = WaitDialog.show(message).setCancelable(true)
        }
        fun dismissWaitDialog() {
            waitDialog?.doDismiss()
            waitDialog = null
        }
        fun showMessageDialog(okButtonClickListener: OnDialogButtonClickListener<MessageDialog>) {
            MessageDialog.show("提示", "是否与该设备进行配对").setOkButton("配对", okButtonClickListener).setCancelButton("取消")
        }
    }
}