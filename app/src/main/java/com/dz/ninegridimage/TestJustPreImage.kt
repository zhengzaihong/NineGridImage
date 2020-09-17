package com.dz.ninegridimage

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dz.ninegridimages.bean.BaseImageBean
import com.dz.ninegridimages.config.IndicatorType
import com.dz.ninegridimages.config.NineGridViewConfigure
import com.dz.ninegridimages.interfaces.ImageLoader
import com.dz.ninegridimages.util.startActivityPre
import com.dz.utlis.ToastTool
import com.dz.utlis.UiCompat
import kotlinx.android.synthetic.main.activity_pre_bgimage.*
import java.util.ArrayList

class TestJustPreImage : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pre_bgimage)

        var urls = ArrayList<String>()


        urls.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg")
        urls.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg")
        urls.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1091405991,859863778&fm=26&gp=0.jpg")
        urls.add("https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3892521478,1695688217&fm=26&gp=0.jpg")

        Glide.with(this).load(urls[0])
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_default_color)
                .override(150, 150)
                .into(imageTest)


        val configure = NineGridViewConfigure()

        with(configure) {
            this.buildPreImageStyleParams().apply {
                this.onPreImageListener = object : ImageLoader.OnPreImageListener {
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
            }

        }

        imageTest.setOnClickListener {
            var bean = BaseImageBean<String>()
            bean.datas = urls
            //传入需要预览的 图片集合
            this.startActivityPre(bean, cf = configure)
        }


    }
}