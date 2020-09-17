
# NineGridImage


依赖地址：

   kotlin版：

    implementation 'com.zzh:ninegridview:0.3.0'


   Java版本 详情使用请往下翻(停止维护)：

    implementation 'com.zzh:ninegridview:0.1.23'



    
    
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

            //以下配置三选一 肯定参数请结合源码
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
        }

        imageTest.setOnClickListener {
            var bean = BaseImageBean<String>()
            bean.datas = urls
            //传入需要预览的 图片集合
            this.startActivityPre(bean, cf =configure)
        }






以下为老版本Java 方式

2019-12-22

1、宫格视图支持代码配置指示器，整体优化

2、支持非宫格浏览大图，具体使用请看文末

注意：如果你的服务端没做多套图加载策略，那图片可能存在很大的情况，因此在使用你图片加载框架时一定的采取合适的加载策略

   

效果简版图：



![Alt text](https://github.com/zhengzaihong/NineGridImage/blob/master/Screenshots/pre.gif)



****************************宫格图片使用说明：***************************



1、布局文件中：


    <com.dz.ninegridimages.view.NineGridView
        android:id="@+id/nineGrid"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

        
2、通过代码方式自定义样式配置：


        final NineGridViewConfigure configure = new NineGridViewConfigure()
                .setSingleImageSize(250)//设置单张图片固定宽高
                .setSingleFixed(false)//设置单张图片固定宽高
                .setRectAdius(10)//设置宫格视图图片圆角度数
                .setColumnNum(2)//设置宫格视图列数
                .setMaxImageSize(9)//设置最大显示多少张
                .setGridSpacing(10)//设置宫格视图的间距
                .setMode(NineGridViewConfigure.MODE_FILL) //设置图片布局模式
                .setSingleImageRatio(1.0f)//设置单张图片的缩放比例
                .setEnablePre(true)//是否开启预览
                .setMoreTextColor(this.getResources().getColor(R.color.amber_200))//设置超过最大张数显示的文本颜色
                .setMoreTextSize(40)//设置超过最大张数显示的字体大小
                .setPreBgColor(this.getResources().getColor(R.color.amber_200)) //设置预览时的背景
                .setPreTipColor(this.getResources().getColor(R.color.red)) //设置指示器文本颜色

                // 指示器新加功能支持 xml 和 代码配置两种方式
                .setEnableIndicatorDot(true) //设置开启 Indicator
                .setIndicatorMargin(10) //设置指示器间距
                //设置自定义指示器 xml 设置会覆盖 代码设置的方式
                //.setIndicator(new int[]{R.drawable.nine_view_indicator_select_dot, R.drawable.nine_view_indicator_un_select_dot}) //优先级最高，其次是代码配置
                .setIndicatorBgPadding(5) //设置Indicator小圆点 背景内距
                .setIndicatorRadiusType(NineGridViewConfigure.RadiusType.ALL_RADIUS) //边距全圆角
                .setIndicatorSize(5) //设置 小圆点指示器大小
                .setIndicatorStrokeColor(this.getResources().getColor(R.color.amber_200)) // 设置Indicator小圆点 边框颜色
                .setIndicatorStrokeWidth(0)//设置Indicator小圆点边框宽度
                .setSelectIndicatorBgColor(this.getResources().getColor(R.color.light_blue_200)) // 设置Indicator小圆点选中时的颜色
                .setUnSelectIndicatorBgColor(this.getResources().getColor(R.color.gray_cc))//设置Indicator小圆点 未选中时的颜色
                //  设置大图预览的长按监听事件
                .setOnPreLongClickListener(new PreImageOnLongClickListener() {
                    @Override
                    public void onImageLongClick(ImageView imageView, int position, Object obejct) {
                        ToastTool.get().show("长按了position=" + position);
                    }
                })
                //设置图片加载器（必须设置） 上面的都是非必须
                .setImageLoader(new ImageLoader() {
                    //九宫格图加载
                    @Override
                    public <T> void displayImage(Context context, ImageView imageView, T object) {

                        //真实开发中，如果你的列表显示 全是图片并多，且勿向这样加载，请异步处理，
                        // 列表在滑动时 不要加载图片，等待停止滑动后做加载。

                        Glide.with(context).load(object.toString())
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.drawable.ic_default_color)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);
                    }

                    //预览大图加载 可以在这里做加载动画等
                    @Override
                    public <T> void loadPreImage(Context context, ImageView imageView, T object) {
                        Glide.with(context).load(object.toString())
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.drawable.ic_default_color)
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(imageView);

                    }

                });
		

3、使用 NineGridView.NineGridViewBuilder 构建并配置所需要的 NineGridViewAdapter,
 setNineAdapter(adapter);
 
 例：


                NineGridView nineGridView = holder.getView(R.id.nineGrid);
                try {
                    //配置并构建adapter
                    NineGridView.NineGridViewBuilder builder = new NineGridView.NineGridViewBuilder(mContext)
                            .setImageInfo(entity.datas)
                            .setNineGridViewConfigure(configure);

                    nineGridView.setNineAdapter(builder.buildAdpter());

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


特别说明：

 1.关键步骤：必须设置setImageLoader
 
 




****************************单独使用大图浏览使用说明：***************************
 
1、第一步


        final NineGridViewConfigure configure = new NineGridViewConfigure()
                .setPreBgColor(this.getResources().getColor(R.color.amber_200)) //设置预览时的背景
                .setPreTipColor(this.getResources().getColor(R.color.red)) //设置指示器文本颜色
              // 指示器新加功能支持 xml 和 代码配置两种方式
                .setEnableIndicatorDot(true) //设置开启 Indicator
                .setIndicatorMargin(10) //设置指示器间距
                //设置自定义指示器 xml 设置会覆盖 代码设置的方式
                //.setIndicator(new int[]{R.drawable.nine_view_indicator_select_dot, R.drawable.nine_view_indicator_un_select_dot}) //优先级最高，其次是代码配置
                .setIndicatorBgPadding(5) //设置Indicator小圆点 背景内距
                .setIndicatorRadiusType(NineGridViewConfigure.RadiusType.ALL_RADIUS) //边距全圆角
                .setIndicatorSize(5) //设置 小圆点指示器大小
                .setIndicatorStrokeColor(this.getResources().getColor(R.color.amber_200)) // 设置Indicator小圆点 边框颜色
                .setIndicatorStrokeWidth(0)//设置Indicator小圆点边框宽度
                .setSelectIndicatorBgColor(this.getResources().getColor(R.color.white)) // 设置Indicator小圆点选中时的颜色
                .setUnSelectIndicatorBgColor(this.getResources().getColor(R.color.gray_cc))//设置Indicator小圆点 未选中时的颜色
                //  设置大图预览的长按监听事件
                .setOnPreLongClickListener(new PreImageOnLongClickListener() {
                    @Override
                    public void onImageLongClick(ImageView imageView, int position, Object obejct) {
                        ToastTool.get().show("长按了position=" + position);
                    }
                })
                //设置图片加载器（必须设置） 上面的都是非必须
                .setImageLoader(new ImageLoader() {
                    //九宫格图加载
                    @Override
                    public <T> void displayImage(Context context, ImageView imageView, T object) {

                        Glide.with(context).load(object.toString())
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.drawable.ic_default_color)
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(imageView);
                    }

                    //预览大图加载 可以在这里做加载动画等
                    @Override
                    public <T> void loadPreImage(Context context, ImageView imageView, T object) {
                        Glide.with(context).load(object.toString())
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.drawable.ic_default_color)
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(imageView);

                    }
                });



2、第二步


         //在需要跳转的地方通过如下方式跳转
         /**
          * 非宫格视图 启动大图预览
          *
          * @param context   上下文
          * @param configure 大图显示的配置i文件
          * @param index     点击的第几个图片，索引
          * @param imageInfo 图片集合
          */
       NineGridViewHelper.getInstance().startPreBigImage(PreBigAcitivity.this, configure, 0, urls);