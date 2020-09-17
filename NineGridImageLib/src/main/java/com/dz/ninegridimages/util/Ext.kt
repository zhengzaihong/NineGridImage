package com.dz.ninegridimages.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dz.ninegridimages.bean.BaseImageBean
import com.dz.ninegridimages.config.NineGridViewConfigure
import com.dz.ninegridimages.preview.ImagePreviewActivity


/**
 * 获得状态栏的高度
 */
fun getStatusHeight(context: Context): Int {
    var statusHeight = -1
    try {
        val clazz = Class.forName("com.android.internal.R\$dimen")
        val obj = clazz.newInstance()
        val height = clazz.getField("status_bar_height")[obj].toString().toInt()
        statusHeight = context.resources.getDimensionPixelSize(height)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return statusHeight
}

/**
 * @param data 显示大图的数据
 * @param index 点击的下标 即从点击位置开始预览
 */

fun <T> Context.startActivityPre(data: BaseImageBean<T>?, index: Int = 0, cf: NineGridViewConfigure) {

    val intent = Intent(this, ImagePreviewActivity::class.java)
    val bundle = Bundle()

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, data)
    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index)
    bundle.putSerializable(ImagePreviewActivity.CONFIGURE,cf)
    intent.putExtras(bundle)

    startActivity(intent)
    (this as Activity).overridePendingTransition(0, 0)

}
