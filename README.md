
# NineGridImage

依赖地址：

   
    implementation 'com.zzh:ninegridview:0.1.21'
   

效果简版图：

![Alt text](https://github.com/zhengzaihong/NineGridImage/blob/master/Screenshots/pre.gif)
![Alt text](https://github.com/zhengzaihong/NineGridImage/blob/master/Screenshots/pre2.gif)
![Alt text](https://github.com/zhengzaihong/NineGridImage/blob/master/Screenshots/singlePic.gif)


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
                    .setColumnNum(3)//设置宫格视图列数
                    .setMaxImageSize(9)//设置最大显示多少张
                    .setGridSpacing(10)//设置宫格视图的间距
                    .setMode(NineGridViewConfigure.MODE_FILL) //设置图片布局模式
                    .setSingleImageRatio(1.0f)//设置单张图片的缩放比例
                    .setEnablePre(true)//是否开启预览
                    .setMoreTextColor(this.getResources().getColor(R.color.amber_200))//设置超过最大张数显示的文本颜色
                    .setMoreTextSize(40)//设置超过最大张数显示的字体大小
                    .setPreBgColor(this.getResources().getColor(R.color.amber_200)) //设置预览时的背景
                    .setPreTipColor(this.getResources().getColor(R.color.red)) //设置指示器文本颜色
    
    //               指示器新加功能支持 xml 和 代码配置两种方式
    
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
                            Glide.with(context).load(object.toString())
                                    .fitCenter()
                                    .placeholder(R.mipmap.ic_launcher)
                                    .error(R.drawable.ic_default_color)
                                    .override(100,100)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageView);
                        }
    
                        //预览大图加载 可以在这里做加载动画等
                        @Override
                        public <T> void loadPreImage(Context context, ImageView imageView, T object) {
                            //实现方式 glide自带监听 4.x 实现方式更加 或者其他自带监听进度的框架
    
                            waitDialog.showDialog();
    
                            Glide.with(context).load(object.toString())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .error(R.drawable.ic_default_color)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            return false;
                                        }
    
                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            waitDialog.dismiss();
                                            return false;
                                        }
                                    })
                                    .into(imageView);
                        }
    
                    });

		

3、通过nineGrid.bindConfigure(configure)绑定即可

4、使用setNineAdapter(new NineGridViewAdapter(MainActivity.this, "图片地址集合"));
 
 例：


        listView.setAdapter(new CommonAdapter<GoodsImage>(this, R.layout.item_news, goodsImages) {
            @Override
            public void convert(ViewHolder holder, int position, GoodsImage entity) {
                ((NineGridView) holder.getView(R.id.nineGrid))
                        .bindConfigure(configure)
                        .setNineAdapter(new NineGridViewAdapter(MainActivity.this, entity.datas));
            }
        });


特别说明：

 1.关键步骤：必须设置setImageLoader
 
 




****************************单独使用大图浏览使用说明：***************************
 
1、第一步


        //创建单图显示的 配置文件
        final SinglePicturePreConfig configure = new SinglePicturePreConfig()
                //设置大图浏览背景
                .setPreBgColor(UiCompat.getColor(getResources(), R.color.yellow_200))
                //设置图片加载器
                .setImageLoader(new SinglePicImageLoader() {
                    @Override
                    public <T> void longClick(Context context, ImageView targetImageView, T object) {

                        ToastTool.get().show("你长按了哟");
                    }

                    @Override
                    public <T> void loadPreImage(Context context, final ImageView transition, final ImageView imageView, T object) {

                        //拿到 Bundle 数据
                        Glide.with(context).load(((Bundle)object).getString("url"))
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.drawable.ic_default_color)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                        transition.setVisibility(View.VISIBLE);
                                        imageView.setVisibility(View.GONE);

                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                        transition.setVisibility(View.GONE);
                                        imageView.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                })
                                .into(imageView);
                    }

                });


            //设置配置文件
            SingleViewPreViewHelper.getInstance().setSinglePicturePreConfig(configure);




2、第二步


     //在需要跳转的地方通过如下方式跳转
     SingleViewPreViewHelper.getInstance().toActClearTop(PreBigAcitivity.this, bundle);