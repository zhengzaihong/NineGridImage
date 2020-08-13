package com.dz.ninegridimage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dz.ninegridimage.bean.GoodsImage
import com.dz.ninegridimages.config.IndicatorType
import com.dz.ninegridimages.config.NineGridViewConfigure
import com.dz.ninegridimages.config.NineGridViewConfigure.Companion.MODE_FILL
import com.dz.ninegridimages.config.RadiusType
import com.dz.ninegridimages.interfaces.ImageLoader
import com.dz.ninegridimages.view.NineGridView
import com.dz.utlis.ToastTool
import com.dz.utlis.UiCompat
import dz.solc.viewtool.adapter.CommonAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mContext: Context? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this

        btGoSingle.setOnClickListener {

            startActivity(Intent(this, TestJustPreImage::class.java))

        }

        val goodsImages: MutableList<GoodsImage<*>> = ArrayList()
        for (i in urls.indices) {
            goodsImages.add(loadData())
        }


        val configure = NineGridViewConfigure()

        with(configure) {

            singleImageSize = 250//设置单张图片固定宽高
            singleFixed = true//设置单张图片固定宽高
            itemImageRadius = 10 //设置宫格视图图片圆角度数
            columnNum = 2 //设置宫格视图列数
            maxImageSize = 9  //设置最大显示多少张
            gridSpacing = 10 //设置宫格视图的间距
            mode = MODE_FILL//设置图片布局模式
            singleImageRatio = 1.0f   //设置单张图片的缩放比例
            enablePre = true //是否开启预览
            moreTextColor = UiCompat.getColor(resources, R.color.amber_200)//设置超过最大张数显示的文本颜色
            moreTextSize = 40f //设置超过最大张数显示的字体大小
            preBgColor = UiCompat.getColor(resources, R.color.amber_200)//设置预览时的背景
            preTipColor = UiCompat.getColor(resources, R.color.red) //设置指示器文本颜色
            indicatorMargin = 10 //设置指示器间距
            //设置指示器 为默认代码方式 如果是xml  方式配合如下 indicator使用
            indicatorType = IndicatorType.XML
            //设置自定义指示器 xml
            indicator = intArrayOf(R.drawable.nine_view_indicator_select_dot, R.drawable.nine_view_indicator_un_select_dot)
            indicatorBgPadding = 10 //设置Indicator小圆点 背景内距
            indicatorRadiusType = RadiusType.ALL_RADIUS//边距全圆角
            indicatorSize = 5 //设置 小圆点指示器大小
            //以下两个属性在CODE 方式中生效
            indicatorStrokeColor = UiCompat.getColor(resources, R.color.amber_200) // 设置Indicator小圆点 边框颜色
            indicatorStrokeWidth = 0 //设置Indicator小圆点边框宽度
            selectIndicatorBgColor = UiCompat.getColor(resources, R.color.light_blue_200) // 设置Indicator小圆点选中时的颜色
            unSelectIndicatorBgColor = UiCompat.getColor(resources, R.color.gray_cc) //设置Indicator小圆点 未选中时的颜色

            this.onPreBigImageListener = object : ImageLoader.OnPreBigImageListener {
                override fun <E : Any?> loadPreImage(context: Context, imageView: ImageView, obj: E, index: Int) {
                    Glide.with(context).load(obj.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.drawable.ic_default_color)
                            .override(150, 150)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView)

                    imageView.setOnLongClickListener {
                        ToastTool.show("长按了哟 $index")
                        false
                    }

                }
            }

            this.onNineGridImageListener = object : ImageLoader.OnNineGridImageListener {

                override fun <T : Any> displayImage(context: Context, imageView: ImageView, obj: T) {

                    //真实开发中，如果你的列表显示 全是图片并多，且勿向这样加载，请异步处理，
                    // 列表在滑动时 不要加载图片，等待停止滑动后做加载。
                    //且应要求后端 分多套分辨图返回
                    Glide.with(context).load(obj.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.drawable.ic_default_color)
                            .override(150, 150)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView)

                }
            }

        }


        listView.adapter = object : CommonAdapter<GoodsImage<String>>(this, R.layout.item_news, goodsImages) {
            override fun convert(holder: ViewHolder, position: Int, entity: GoodsImage<String>) {
                val nineGridView = holder.getView<NineGridView<String>>(R.id.nineGrid)

                nineGridView.setData(entity.datas as List<String>, configure)
            }
        }
    }

    private fun loadData(): GoodsImage<String> {
        val imageBean: GoodsImage<String> = GoodsImage<String>()
        imageBean.datas = randomUrl()
        return imageBean
    }

    private fun randomUrl(): List<String> {
        val stringList: MutableList<String> = ArrayList()
        for (i in 0..8) {
            stringList.add(urls[Random().nextInt(urls.size)])
        }
        return stringList
    }

    companion object {
        var urls: MutableList<String> = ArrayList()

        init {
            urls.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg")
            urls.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg")
            urls.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1091405991,859863778&fm=26&gp=0.jpg")
            urls.add("https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3892521478,1695688217&fm=26&gp=0.jpg")
            urls.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3496844071,1524827572&fm=26&gp=0.jpg")
            urls.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2853553659,1775735885&fm=26&gp=0.jpg")
            urls.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2853553659,1775735885&fm=26&gp=0.jpg")
            urls.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2612816471,1892359600&fm=26&gp=0.jpg")
            urls.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=911096125,954471894&fm=26&gp=0.jpg")
            urls.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2542449824,3892511278&fm=26&gp=0.jpg")
            urls.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=275303868,798579082&fm=26&gp=0.jpg")
        }
    }
}