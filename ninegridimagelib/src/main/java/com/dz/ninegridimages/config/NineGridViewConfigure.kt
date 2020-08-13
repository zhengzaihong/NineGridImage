package com.dz.ninegridimages.config

import android.graphics.Color
import com.dz.ninegridimages.interfaces.ImageLoader
import java.io.Serializable

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * create_date: 2019/1/22 0022
 * create_time: 14:47
 * describe: 宫格视图参数配置
 */
class NineGridViewConfigure : Serializable {


    companion object {
        //填充模式，类似于微信
        const val MODE_FILL = 0

        //网格模式，类似于QQ，4张图会 2X2布局
        const val MODE_GRID = 1
    }

    //设置九宫格图片圆角度数
    var itemImageRadius = 0

    //设置指示器drawable id
    var indicator: IntArray? = null

    //设置指示器的实现方式
    var indicatorType = IndicatorType.TEXT

    //文本指示器字体颜色
    var preTipColor = Color.WHITE

    //预览背景色
    var preBgColor = Color.BLACK

    //指示器边距
    var indicatorMargin = 8

    //设置列数
    var columnNum = 3

    // 单张图片时的最大大小,单位px
    var singleImageSize = 250

    //单张照片时是否固定宽高
    var singleFixed = false

    // 单张图片的宽高比(宽/高)
    var singleImageRatio = 1.0f

    // 最大显示的图片数
    var maxImageSize = 9

    // 宫格间距，单位px
    var gridSpacing = 3

    // 默认使用fill模式
    var mode = MODE_FILL

    //显示文字的大小单位px
    var moreTextSize = 35f

    //显示文字的颜色
    var moreTextColor = Color.parseColor("#E0A0D0")

    //设置指示器字体大小
    var preTipTextSize = 22

    //开启预览
    var enablePre = true

    //选中时的背景颜色
    var selectIndicatorBgColor = Color.parseColor("#5A8BF4")

    //未选中时的背景颜色
    var unSelectIndicatorBgColor = Color.parseColor("#5A8BF4")

    //指示器背景圆角度数
    var indicatorBgRadius = 30

    // 指示器边框线宽
    var indicatorStrokeWidth = 0

    //指示器 边框颜色
    var indicatorStrokeColor = Color.TRANSPARENT

    //指示器背景的 内距
    var indicatorBgPadding = 0

    //指示器圆角类型
    var indicatorRadiusType = RadiusType.ALL_RADIUS

    //指示器的大小
    var indicatorSize = 30

    //显示宫格视图的 监听器
    var onNineGridImageListener: ImageLoader.OnNineGridImageListener? = null

    // 加载预览大图的监听器
    var onPreBigImageListener:ImageLoader.OnPreBigImageListener? = null

}


