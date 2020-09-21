
# NineGridImage



效果简版图：



<img src="https://github.com/zhengzaihong/NineGridImage/blob/master/images/pre-min.gif" width="400" height="600" alt="note"/>


依赖地址：

   kotlin版：

    implementation 'com.zzh:ninegridview:0.3.0'




更新日志：

2020-9-15

1.使用更加简洁，快速稳定性更强。

2.修改和优化不雅的策略方式。

3.支持普通单图和多图预览配置



****************************宫格图片使用说明：***************************

使用NineGridView加载视图:

1、布局文件中：


       <com.dz.ninegridimages.view.NineGridView
           android:id="@+id/nineGrid"
           android:layout_width="match_parent"
           android:layout_height="wrap_content" />



2、通过代码方式自定义样式配置：


        //创建配置文件
        val configure = NineGridViewConfigure()

        //配置NineGridView 参数(不设置 默认提供)，图片监听回调除外
        with(configure.buildBaseStyleParams()){

            singleImageSize = 250//设置单张图片固定宽高
            singleFixed = true//设置单张图片固定宽高
            itemImageCorner = 10 //设置宫格视图图片圆角度数
            columnNum = 2 //设置宫格视图列数
            maxImageSize = 9  //设置最大显示多少张
            gridSpacing = 10 //设置宫格视图的间距
            mode = MODE_FILL//设置图片布局模式
            singleImageRatio = 1.0f   //设置单张图片的缩放比例
            moreTextColor = UiCompat.getColor(resources, R.color.amber_200)//设置超过最大张数显示的文本颜色
            moreTextSize = 40f //设置超过最大张数显示的字体大小

            // 必须设置，否则不加载图片
            onNineGridImageListener = object : ImageLoader.OnNineGridImageListener {
                override fun <T : Any> displayImage(context: Context, imageView: ImageView, obj: T) {

                    //真实开发中，如果你的列表显示 全是图片并多，
                    // 列表在滑动时 不要加载图片，等待停止滑动后做加载。
                    //且应要求后端 分多套分辨图返回
                    Glide.with(context).load(obj.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.drawable.ic_default_color)
                            .override(150, 150)
                            .into(imageView)

                }
            }
        }

        //可选参数 配置
        with(configure.buildPreImageStyleParams()) {
            enablePre = true //是否开启预览
            preBgColor = UiCompat.getColor(resources, R.color.amber_200)//设置预览时的背景

            //加载图片的必须设置
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

        with(configure.buildIndicatorStyleParams()){

            //以下配置三选一 其他参数请结合源码
            //代码指示器
            this.buildStyleCode().apply {

                distanceType = ViewPagerIndicator.DistanceType.BY_RADIUS
                indicatorCorner = 5
                vpIndicatorType = ViewPagerIndicator.VpIndicatorType.BEZIER

                indicatorViewHeight = 30 //设置指示器父容器高度，需要配合 indicatorSize 设置.
                indicatorMargin = 10 //设置指示器间距
                indicatorSize = 10 //设置 小圆点指示器大小

                //以下两个属性在CODE 方式中生效
                selectIndicatorBgColor = UiCompat.getColor(resources, R.color.red) // 设置Indicator小圆点选中时的颜色
                unSelectIndicatorBgColor = UiCompat.getColor(resources, R.color.gray_cc) //设置Indicator小圆点 未选中时的颜色

            }

             //xml 布局方式
            this.buildStyleXml().apply {
                //设置自定义指示器 xml
                indicator = intArrayOf(R.drawable.nine_view_indicator_select_dot, R.drawable.nine_view_indicator_un_select_dot)

            }

            //文本显示
            this.buildStyleText().apply {
                preTipColor = UiCompat.getColor(resources, R.color.red) //设置指示器文本颜色
                preTipTextSize = ScreenUtils.sp2px(this@MainActivity, 12f).toInt()
            }


3.绑定数据：

     //支持泛型的数据
     nineGridView.setData(entity.datas, configure)



****************************单独使用大图浏览使用说明：***************************


        val configure = NineGridViewConfigure()

        with(configure) {

            this.indicatorType = IndicatorType.TEXT
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

        imageTest.setOnClickListener {
            var bean = BaseImageBean<String>()
            bean.datas = urls
            //传入需要预览的 图片集合
            this.startActivityPre(bean, cf =configure)
        }




