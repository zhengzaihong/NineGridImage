package com.dz.ninegridimages.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.dz.ninegridimages.R
import com.dz.ninegridimages.bean.BaseImageBean
import com.dz.ninegridimages.config.NineGridViewConfigure
import com.dz.ninegridimages.config.NineGridViewConfigure.Companion.MODE_GRID
import com.dz.ninegridimages.util.getStatusHeight
import com.dz.ninegridimages.util.startActivityPre


/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * create_date: 2019/1/17 0017
 * create_time: 16:11
 * describe: 网格视图
 */

@SuppressWarnings("all")
class NineGridView<T> constructor(
        context: Context,
        attrs: AttributeSet?
) : RelativeLayout(context, attrs) {


    private var imageViews = mutableListOf<RoundIconView>()
    private var mImageInfo: List<T>? = null

    // 列数
    private var columnCount = 0

    // 行数
    private var rowCount = 0

    // 宫格宽度
    private var gridWidth = 0

    // 宫格高度
    private var gridHeight = 0

    private val statusHeight = getStatusHeight(context)
    private var configure = NineGridViewConfigure()


    /**
     * 获得 ImageView 保证了 ImageView 的重用
     */
    private fun getImageView(position: Int): RoundIconView {
        val imageView: RoundIconView
        if (position < imageViews.size) {
            imageView = imageViews[position]
        } else {
            imageView = generateImageView(context)
            if (configure.itemImageRadius > 0) imageView.setRectRadius(configure.itemImageRadius.toFloat())

            imageView.setOnClickListener {

                if (!configure.enablePre || mImageInfo.isNullOrEmpty()) {
                    return@setOnClickListener
                }

                val imageBean = BaseImageBean<T>()

                mImageInfo?.let {
                    for (i in it.indices) {
                        try {
                            var imageView = if (i < configure.maxImageSize) {
                                getChildAt(i)
                            } else {
                                //如果图片的数量大于显示的数量，则超过部分的返回动画统一退回到最后一个图片的位置
                                getChildAt(configure.maxImageSize - 1)
                            }

                            with(imageBean) {
                                imageViewWidth = imageView.width
                                imageViewHeight = imageView.height
                                val points = IntArray(2)
                                imageView.getLocationInWindow(points)
                                imageViewX = points[0]
                                imageViewY = points[1] - statusHeight
                                datas = it
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                Log.v("------->", imageBean.toString())
                context.startActivityPre(imageBean, position, configure)


            }
            imageViews.add(imageView)
        }
        return imageView
    }

    /**
     * 生成ImageView容器的方式，默认使用NineGridImageViewWrapper类，即点击图片后，图片会有蒙板效果
     * 如果需要自定义图片展示效果，重写此方法即可
     *
     * @param context 上下文
     * @return 生成的 ImageView
     */
    private fun generateImageView(context: Context?): RoundIconView {
        val imageView = NineGridViewWrapper(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.ic_default_color)
        return imageView
    }

    fun setData(imageInfo: List<T>, cf: NineGridViewConfigure = configure): NineGridView<T> {

        if (imageInfo.isNullOrEmpty()) {
            visibility = View.GONE
            return this
        }

        //更新外部转入的 configure
        this.configure = cf
        visibility = View.VISIBLE
        var imageCount = imageInfo.size

        if (configure.maxImageSize in 1 until imageCount) {
            //再次获取图片数量
            imageCount = imageInfo.subList(0, configure.maxImageSize).size
        }

        rowCount = imageCount / configure.columnNum + if (imageCount % configure.columnNum == 0) 0 else 1
        columnCount = configure.columnNum


        //grid模式下，显示4张使用2X2模式
        if (configure.mode == MODE_GRID) {
            if (imageCount == 4) {
                rowCount = 2
                columnCount = 2
            }
        }

        //保证View的复用，避免重复创建
        if (mImageInfo == null) {
            for (i in 0 until imageCount) {
                val imageView = getImageView(i) ?: return this
                if (imageCount == 1) {
                    val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    layoutParams.addRule(CENTER_IN_PARENT)
                    imageView.layoutParams = layoutParams

                    val layoutParams1 = generateDefaultLayoutParams() as LayoutParams
                    layoutParams1.width = LayoutParams.MATCH_PARENT
                    addView(imageView, layoutParams1)
                } else {
                    addView(imageView, generateDefaultLayoutParams())
                }
            }
        } else {
            val oldViewCount = imageInfo?.size!!
            val newViewCount = imageCount
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount, oldViewCount - newViewCount)
            } else if (oldViewCount < newViewCount) {
                for (i in oldViewCount until newViewCount) {
                    val iv = getImageView(i) ?: return this
                    addView(iv, generateDefaultLayoutParams())
                }
            }
        }
        //修改最后一个条目，决定是否显示更多
        if (imageInfo.size > configure.maxImageSize) {
            val child = getChildAt(configure.maxImageSize - 1)
            if (child is NineGridViewWrapper) {

                with(child) {
                    moreNum = imageInfo.size - configure.maxImageSize
                    textColor = configure.moreTextColor
                    textSize = configure.moreTextSize
                }

            }
        }
        mImageInfo = imageInfo
        requestLayout()
        return this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0
        val totalWidth = width - paddingLeft - paddingRight
        if (!mImageInfo.isNullOrEmpty()) {
            if (mImageInfo!!.size == 1) {
                gridWidth = if (configure.singleImageSize > totalWidth) totalWidth else configure.singleImageSize
                gridHeight = (gridWidth / configure.singleImageRatio).toInt()
                //矫正图片显示区域大小，不允许超过最大显示范围
                if (gridHeight > configure.singleImageSize) {
                    if (!configure.singleFixed) {
                        val ratio = configure.singleImageSize * 1.0f / gridHeight
                        gridWidth = (gridWidth * ratio).toInt()
                    }
                    gridHeight = configure.singleImageSize
                }
            } else {
//                gridWidth = gridHeight = (totalWidth - gridSpacing * (columnCount - 1)) / columnCount;
                //这里无论是几张图片，宽高都按总宽度的 1/columnCount
                gridHeight = (totalWidth - configure.gridSpacing * 2) / columnCount
                gridWidth = gridHeight
            }
            width = gridWidth * columnCount + configure.gridSpacing * (columnCount - 1) + paddingLeft + paddingRight
            height = gridHeight * rowCount + configure.gridSpacing * (rowCount - 1) + paddingTop + paddingBottom
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (mImageInfo == null) return
        val childrenCount = mImageInfo?.size!!
        for (i in 0 until childrenCount) {
            val childrenView = getChildAt(i) as ImageView
            val rowNum = i / columnCount
            val columnNum = i % columnCount
            val left = (gridWidth + configure.gridSpacing) * columnNum + paddingLeft
            val top = (gridHeight + configure.gridSpacing) * rowNum + paddingTop
            val right = left + gridWidth
            val bottom = top + gridHeight
            childrenView.layout(left, top, right, bottom)
            configure?.onNineGridImageListener?.displayImage(context, childrenView, mImageInfo!![i])
        }
    }

}