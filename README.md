# NineGridImage

依赖地址：

compile 'com.zzh:ninegridview:0.1.0'

效果简版图：

![Alt text](https://github.com/zhengzaihong/NineGridImage/blob/master/Screenshots/Screenshot_2019-01-23-15-10-51-40.png)
![Alt text](https://github.com/zhengzaihong/NineGridImage/blob/master/Screenshots/Screenshot_2019-01-23-15-10-57-61.png)
![Alt text](https://github.com/zhengzaihong/NineGridImage/blob/master/Screenshots/Screenshot_2019-01-23-15-41-13-59.png)


使用说明：


1、布局文件中：

    <com.dz.ninegridimages.view.NineGridView
        android:id="@+id/nineGrid"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
        
2、通过代码方式自定义样式配置：

        final NineGridViewConfigure configure = NineGridViewConfigure.getNineGridViewConfigure()
		 //设置单张图片固定宽高
                .setSingleImageSize(250)
		//设置单张图片固定宽高
                .setSingleFixed(false)
		//设置宫格视图图片圆角度数
                .setRectAdius(10)
		//设置宫格视图列数
                .setColumnNum(3)
		//设置最大显示多少张
                .setMaxImageSize(9)
		//设置宫格视图的间距
                .setGridSpacing(10)
		//设置图片布局模式
                .setMode(NineGridView.MODE_FILL)
		//设置单张图片的缩放比例
                .setSingleImageRatio(1.0f)
		//设置超过最大张数显示的文本颜色
                .setMoreTextColor(this.getResources().getColor(R.color.amber_200))
		//设置超过最大张数显示的字体大小
                .setMoreTextSize(40)
		//设置预览时的背景
                .setPreBgColor(this.getResources().getColor(R.color.amber_200))
		//设置指示器间距
                .setIndicatorMargin(10) 
		//设置指示器文本颜色   注意：setIndicator 优先级更高
                .setPreTipColor(this.getResources().getColor(R.color.red)) 
                //设置自定义指示器
              //.setIndicator(new int[]{R.drawable.nine_view_indicator_select_dot, R.drawable.nine_view_indicator__un_select_dot})
                //  设置大图预览的长按监听事件
                .setOnPreLongClickListener(new PreImageOnLongClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onImageLongClick(ImageView imageView, int position) {
                        Toast.makeText(MainActivity.this, "长按了哟position=" + position, 1).show();
                    }
                })
                //设置图片加载器（必须设置）
                .setImageLoader(new ImageLoader<BaseImageBean>() {
                    //九宫格图加载
                    @Override
                    public void onDisplayImage(Context context, ImageView imageView, BaseImageBean object) {
                        Glide.with(context).load(object.getImageUrl())
                                .fitCenter()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.drawable.ic_default_color)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);
                    }

                    //预览大图加载 可以在这里做加载动画等
                    @Override
                    public void loadPreImage(Context context, ImageView imageView, BaseImageBean object) {

                        //实现方式 glide自带监听 4.x 实现方式更加 或者其他自带监听进度的框架
                        final WaitDialog waitDialog = new WaitDialog(context);
                        waitDialog.show();
                        Glide.with(context).load(object.getImageUrl())
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.drawable.ic_default_color)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
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

特别说明：

 1.关键步骤：必须设置setImageLoader
 
 2.在开发中都是请求的接口拿到带有图片地址的对象，所以传入的图片对象需要继承BaseImageBean
 




