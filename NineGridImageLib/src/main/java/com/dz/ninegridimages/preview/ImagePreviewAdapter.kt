package com.dz.ninegridimages.preview

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.dz.ninegridimages.R
import com.dz.ninegridimages.config.NineGridViewConfigure.NineGridViewPreImageStyleParams
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener

/**
 * create_user: zhengzaihong
 * email:1096877329@qq.com
 * create_date: 2019/1/22 0022
 * create_time: 16:11
 * describe: 大图的适配器
 */
open class ImagePreviewAdapter<T>(val context: Context, val configure: NineGridViewPreImageStyleParams, val imageInfo: List<T>) : PagerAdapter(), OnPhotoTapListener {


    var primaryItem: View? = null

    override fun getCount(): Int {
        return imageInfo?.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
        super.setPrimaryItem(container, position, obj)
        primaryItem = obj as View
    }


    open fun getPrimaryImageView(): ImageView? {
        return primaryItem as ImageView?
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false) as PhotoView
        imageView.adjustViewBounds = true
        imageView.onPhotoTapListener = this
        val info = imageInfo!![position]
        showExcessPic(imageView)
        configure.onPreImageListener?.loadPreImage(context, imageView, info,position)
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        if (obj is PhotoView) {
            container.removeView(obj)
//            releaseImageViewResouce(obj as ImageView)
        }
    }

    /**
     * 展示过度图片
     */
    private fun showExcessPic(imageView: PhotoView) {
        imageView.setImageResource(R.drawable.ic_default_color)
    }

    /**
     * 单击屏幕关闭
     */
    override fun onPhotoTap(view: View, x: Float, y: Float) {
        (context as ImagePreviewActivity<*>).finishActivityAnim()

//        releaseImageViewResouce(view as ImageView)
    }

    /**
     * 释放图片资源的方法
     *
     * @param imageView
     */
    private fun releaseImageViewResouce(imageView: ImageView?) {
        if (imageView == null) return
        val drawable = imageView.drawable
        if (drawable != null && drawable is BitmapDrawable) {
            var bitmap = drawable.bitmap
            if (bitmap != null && !bitmap.isRecycled) {
                bitmap.recycle()
            }
        }
    }

}