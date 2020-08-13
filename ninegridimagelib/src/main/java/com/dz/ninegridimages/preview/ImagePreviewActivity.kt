package com.dz.ninegridimages.preview

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.dz.ninegridimages.R
import com.dz.ninegridimages.bean.BaseImageBean
import com.dz.ninegridimages.config.IndicatorType
import com.dz.ninegridimages.config.NineGridViewConfigure
import com.dz.ninegridimages.view.IndicatorView
import kotlinx.android.synthetic.main.activity_preview.*

/**
 * create_user: zhengzaihong
 * email:1096877329@qq.com
 * create_date: 2019/1/22 0022
 * create_time: 16:11
 * describe: 预览视图界面
 *  2020/8/10 扩展 支持非宫格视图 大图预览
 *
 */

@SuppressWarnings("all")
class ImagePreviewActivity<T> : Activity(), ViewTreeObserver.OnPreDrawListener {

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1.0f)
    private lateinit var imagePreviewAdapter: ImagePreviewAdapter<T>
    private lateinit var mContext: Context
    private lateinit var configure: NineGridViewConfigure


    private var currentItem = 0
    private var imageHeight = 0
    private var imageWidth = 0
    private var screenWidth = 0
    private var screenHeight = 0

    private lateinit var imageInfo: BaseImageBean<T>
    private var preImageViews = ArrayList<ImageView>()

    companion object {
        const val IMAGE_INFO = "IMAGE_INFO"
        const val CURRENT_ITEM = "CURRENT_ITEM"
        const val ANIMATE_DURATION = 200
        const val CONFIGURE = "CONFIGURE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        mContext = this
        val metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)
        screenWidth = metric.widthPixels
        screenHeight = metric.heightPixels

        val bundle = intent.extras

        configure = bundle.getSerializable(CONFIGURE) as NineGridViewConfigure
        imageInfo = bundle.getSerializable(IMAGE_INFO) as BaseImageBean<T>
        currentItem = bundle.getInt(CURRENT_ITEM, 0)

        imagePreviewAdapter = ImagePreviewAdapter(this, configure, imageInfo?.datas)

        val indicator = configure.indicator
        viewPager.adapter = imagePreviewAdapter
        viewPager.currentItem = currentItem
        viewPager.viewTreeObserver.addOnPreDrawListener(this)
        initPage(indicator)

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                currentItem = position

                when (configure.indicatorType) {
                    IndicatorType.XML -> {
                        indicator?.let {
                            for (i in preImageViews.indices) {
                                preImageViews[i]!!.setBackgroundResource(if (i == position) it[0] else it[1])
                            }
                        }
                    }
                    IndicatorType.CODE -> {
                        for (i in preImageViews.indices) {
                            (preImageViews[i] as IndicatorView).changView(i == position)
                        }
                    }

                    //显示文本指示器
                    IndicatorType.TEXT -> {
                        tvPager.text = String.format(getString(R.string.select), imageInfo.datas.size, currentItem + 1)
                    }

                }
            }
        })
    }


    private fun initPage(indicator: IntArray?) {


        when (configure.indicatorType) {
            IndicatorType.XML -> {
                llIndicator.visibility = View.VISIBLE
                tvPager.visibility = View.GONE
                indicator?.let {
                    for (i in imageInfo!!.datas!!.indices) {
                        val imageView = ImageView(mContext)
                        imageView.layoutParams = LayoutParams(15, 15)
                        preImageViews.add(imageView)
                        preImageViews[i].setBackgroundResource(if (i == currentItem) indicator[0] else indicator[1])

                        val layoutParams = LinearLayout.LayoutParams(LayoutParams(
                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

                        layoutParams.leftMargin = configure.indicatorMargin

                        llIndicator.addView(imageView, layoutParams)
                    }
                }
            }

            IndicatorType.CODE -> {
                llIndicator.visibility = View.VISIBLE
                tvPager.visibility = View.GONE
                //代码方式
                for (i in imageInfo!!.datas!!.indices) {
                    val imageView = IndicatorView(mContext)
                    val boxSize = configure.indicatorSize
                    imageView.layoutParams = LayoutParams(boxSize, boxSize)

                    //默认第一个是选中
                    imageView.setConfig(configure, currentItem == i)
                    preImageViews.add(imageView)
                    val layoutParams = LinearLayout.LayoutParams(LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
                    layoutParams.leftMargin = configure!!.indicatorMargin
                    llIndicator.addView(imageView, layoutParams)
                }
            }

            //显示文本指示器
            IndicatorType.TEXT -> {
                llIndicator.visibility = View.GONE
                tvPager.visibility = View.VISIBLE
                tvPager.textSize = configure.preTipTextSize.toFloat()
                tvPager.setTextColor(configure.preTipColor)
                tvPager.text = String.format(getString(R.string.select), imageInfo.datas.size, currentItem + 1)
            }
        }
    }


    override fun onBackPressed() {
        finishActivityAnim()
    }

    override fun onDestroy() {
        super.onDestroy()
        valueAnimator.cancel()
    }

    /**
     * 绘制前开始动画
     */
    override fun onPreDraw(): Boolean {
        rootView.viewTreeObserver.removeOnPreDrawListener(this)
        val view = imagePreviewAdapter.primaryItem
        val imageView = imagePreviewAdapter.getPrimaryImageView()
        computeImageWidthAndHeight(imageView)

        val imageData = imageInfo
        val vx = imageData!!.imageViewWidth * 1.0f / imageWidth
        val vy = imageData.imageViewHeight * 1.0f / imageHeight
        valueAnimator.addUpdateListener { animation ->
            val duration = animation.duration
            val playTime = animation.currentPlayTime
            var fraction = if (duration > 0) playTime.toFloat() / duration else 1f
            if (fraction > 1) fraction = 1f

            view?.let {
                it.translationX = evaluateInt(fraction, imageData.imageViewX + imageData.imageViewWidth / 2 - imageView!!.width / 2, 0).toFloat()
                it.translationY = evaluateInt(fraction, imageData.imageViewY + imageData.imageViewHeight / 2 - imageView!!.height / 2, 0).toFloat()
                it.scaleX = evaluateFloat(fraction, vx, 1)
                it.scaleY = evaluateFloat(fraction, vy, 1)
                it.alpha = fraction
            }

            rootView.setBackgroundColor(evaluateArgb(fraction, Color.TRANSPARENT, configure.preBgColor))
        }
        addIntoListener(valueAnimator)
        valueAnimator.duration = ANIMATE_DURATION.toLong()
        valueAnimator.start()
        return true
    }

    /**
     * activity的退场动画
     */
    fun finishActivityAnim() {
        val view = imagePreviewAdapter!!.primaryItem
        val imageView = imagePreviewAdapter.getPrimaryImageView()!!
        computeImageWidthAndHeight(imageView)
        val imageData = imageInfo
        val vx = imageData!!.imageViewWidth * 1.0f / imageWidth
        val vy = imageData.imageViewHeight * 1.0f / imageHeight
        valueAnimator.addUpdateListener { animation ->
            val duration = animation.duration
            val playTime = animation.currentPlayTime
            var fraction = if (duration > 0) playTime.toFloat() / duration else 1f
            if (fraction > 1) fraction = 1f

            view?.let {
                it.translationX = evaluateInt(fraction, 0, imageData.imageViewX + imageData.imageViewWidth / 2 - imageView.width / 2).toFloat()
                it.translationY = evaluateInt(fraction, 0, imageData.imageViewY + imageData.imageViewHeight / 2 - imageView.height / 2).toFloat()
                it.scaleX = evaluateFloat(fraction, 1, vx)
                it.scaleY = evaluateFloat(fraction, 1, vy)
                it.alpha = 1 - fraction
            }

            rootView.setBackgroundColor(evaluateArgb(fraction, Color.BLACK, Color.TRANSPARENT))
        }
        addOutListener(valueAnimator)
        valueAnimator.duration = ANIMATE_DURATION.toLong()
        valueAnimator.start()
    }

    /**
     * 计算图片的宽高
     */
    private fun computeImageWidthAndHeight(imageView: ImageView?) {

        if (null == imageView) {
            return
        }
        // 获取真实大小
        var drawable = imageView.drawable
        val intrinsicHeight = drawable!!.intrinsicHeight
        val intrinsicWidth = drawable.intrinsicWidth
        // 计算出与屏幕的比例，用于比较以宽的比例为准还是高的比例为准，因为很多时候不是高度没充满，就是宽度没充满
        var h = screenHeight * 1.0f / intrinsicHeight
        var w = screenWidth * 1.0f / intrinsicWidth
        if (h > w) h = w else w = h

        // 得出当宽高至少有一个充满的时候图片对应的宽高
        imageHeight = (intrinsicHeight * h).toInt()
        imageWidth = (intrinsicWidth * w).toInt()
    }

    /**
     * 进场动画过程监听
     */
    private fun addIntoListener(valueAnimator: ValueAnimator) {
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                rootView!!.setBackgroundColor(0x0)
            }

            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    /**
     * 退场动画过程监听
     */
    private fun addOutListener(valueAnimator: ValueAnimator) {
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                rootView!!.setBackgroundColor(0x0)
            }

            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    /**
     * Integer 估值器
     */
    private fun evaluateInt(fraction: Float, startValue: Int, endValue: Int): Int {
        return (startValue + fraction * (endValue - startValue)).toInt()
    }

    /**
     * Float 估值器
     */
    private fun evaluateFloat(fraction: Float, startValue: Number, endValue: Number): Float {
        val startFloat: Float = startValue.toFloat()
        return startFloat + fraction * (endValue.toFloat() - startFloat)
    }

    /**
     * Argb 估值器
     */
    private fun evaluateArgb(fraction: Float, startValue: Int, endValue: Int): Int {
        val startA = startValue shr 24 and 0xff
        val startR = startValue shr 16 and 0xff
        val startG = startValue shr 8 and 0xff
        val startB = startValue and 0xff
        val endA = endValue shr 24 and 0xff
        val endR = endValue shr 16 and 0xff
        val endG = endValue shr 8 and 0xff
        val endB = endValue and 0xff
        return startA + (fraction * (endA - startA)).toInt() shl 24 or
                (startR + (fraction * (endR - startR)).toInt() shl 16) or
                (startG + (fraction * (endG - startG)).toInt() shl 8) or
                startB + (fraction * (endB - startB)).toInt()
    }


}