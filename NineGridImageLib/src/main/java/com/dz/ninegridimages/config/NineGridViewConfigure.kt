package com.dz.ninegridimages.config

import android.graphics.Color
import com.dz.ninegridimages.R
import com.dz.ninegridimages.interfaces.ImageLoader
import com.dz.ninegridimages.view.ViewPagerIndicator
import java.io.Serializable
import java.lang.RuntimeException

/**
 * create_user: zhengzaihong
 * email:1096877329@qq.com
 * create_date: 2019/1/22 0022
 * create_time: 14:47
 * describe: 宫格视图参数配置
 */
class NineGridViewConfigure : Serializable {


    /**
     * NineGridView 填充类型
     *
     */
    companion object {
        //填充模式，类似于微信
        const val MODE_FILL = 0

        //网格模式，类似于QQ，4张图会 2X2布局
        const val MODE_GRID = 1
    }


    private var nineGridViewBaseStyleParams = NineGridViewBaseStyleParams()
    private var nineGridViewPreImageStyleParams = NineGridViewPreImageStyleParams()
    private var nineGridViewIndicatorStyleParams = NineGridViewIndicatorStyleParams()


    open fun buildBaseStyleParams(): NineGridViewBaseStyleParams {
        return nineGridViewBaseStyleParams
    }

    open fun buildPreImageStyleParams(): NineGridViewPreImageStyleParams {
        return nineGridViewPreImageStyleParams
    }

    open fun buildIndicatorStyleParams(): NineGridViewIndicatorStyleParams {
        return nineGridViewIndicatorStyleParams
    }


    /**
     * NineGridView 基础样式配置参数
     */
    open class NineGridViewBaseStyleParams : Serializable {

        //设置九宫格图片圆角度数
        var itemImageCorner = 0

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

        //显示宫格视图的 监听器
        var onNineGridImageListener: ImageLoader.OnNineGridImageListener? = null

    }


    /**
     * 大图预览相关参数
     */
    open class NineGridViewPreImageStyleParams : Serializable {

        //开启预览
        var enablePre = true

        //预览背景色
        var preBgColor = Color.BLACK

        // 加载预览大图的监听器
        var onPreImageListener: ImageLoader.OnPreImageListener? = null

    }


    /**
     * 大图预览时指示器的样式参数
     */
    open class NineGridViewIndicatorStyleParams:Serializable {

        //设置指示器的实现方式
        private var indicatorType = IndicatorType.TEXT

        private var indicatorText = NineGridViewIndicatorText()
        private var indicatorXml = NineGridViewIndicatorXml()
        private var indicatorCode = NineGridViewIndicatorCode()


        open fun buildStyleText(indicatorType: IndicatorType = IndicatorType.TEXT): NineGridViewIndicatorText {
            this.indicatorType = indicatorType
            return indicatorText
        }

        open fun buildStyleXml(indicatorType: IndicatorType = IndicatorType.XML): NineGridViewIndicatorXml {
            this.indicatorType = indicatorType
            return indicatorXml
        }

        open fun buildStyleCode(indicatorType: IndicatorType = IndicatorType.CODE): NineGridViewIndicatorCode {
            this.indicatorType = indicatorType
            return indicatorCode
        }

        open fun styleType(): IndicatorType {
            return indicatorType
        }


        open class NineGridViewIndicatorBase : Serializable {
            //指示器边距
            var indicatorMargin = 8

            //指示器大小
            var indicatorSize = 15

            //选中时的背景颜色
            var selectIndicatorBgColor = Color.parseColor("#5A8BF4")

            //未选中时的背景颜色
            var unSelectIndicatorBgColor = Color.parseColor("#5A8BF4")

        }


        /**
         * 资源xml 方式，样式更加灵活又外部开发者定义
         */
        open class NineGridViewIndicatorXml : NineGridViewIndicatorBase() {

            //设置指示器drawable [id]，，不设置默认系统提供
            var indicator: IntArray = intArrayOf(R.drawable.nine_view_indicator_select_dot,
                    R.drawable.nine_view_indicator_un_select_dot)

        }


        /**
         * 文字文本方式
         */
        open class NineGridViewIndicatorText : NineGridViewIndicatorBase() {

            //文本指示器字体颜色
            var preTipColor = Color.WHITE


            //设置指示器字体大小
            var preTipTextSize = 22

        }


        /**
         * 代码方式
         */
        open class NineGridViewIndicatorCode : NineGridViewIndicatorBase() {

            //指示器背景圆角度数
            var indicatorCorner = 30

            //指示器容器的高度
            var indicatorViewHeight = 30

            //距离类型 code 方式生效
            var distanceType = ViewPagerIndicator.DistanceType.BY_RADIUS

            //指示器类型 code 方式生效
            var vpIndicatorType = ViewPagerIndicator.VpIndicatorType.CIRCLE

        }

    }

}


