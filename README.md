
# NineGridImage

依赖地址：

compile 'com.zzh:ninegridview:0.1.2'

效果简版图：

![Alt text](https://github.com/zhengzaihong/NineGridImage/blob/master/Screenshots/pre.gif)
![Alt text](https://github.com/zhengzaihong/NineGridImage/blob/master/Screenshots/pre2.gif)

使用说明：


1、布局文件中：


    <com.dz.ninegridimages.view.NineGridView
        android:id="@+id/nineGrid"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />


        
2、通过代码方式自定义样式配置：


      NineGridViewConfigure configure = new NineGridViewConfigure()
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
                .setMode(NineGridViewConfigure.MODE_FILL)
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
                //是否开启预览
                .setEnablePre(true)
		        //设置指示器文本颜色   注意：setIndicator 优先级更高
                .setPreTipColor(this.getResources().getColor(R.color.red)) 
                //设置自定义指示器
               //.setIndicator(new int[]{R.drawable.nine_view_indicator_select_dot,R.drawable.nine_view_indicator__un_select_dot})
                //  设置大图预览的长按监听事件
                .setOnPreLongClickListener(new PreImageOnLongClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onImageLongClick(ImageView imageView, int position) {
                        Toast.makeText(MainActivity.this, "长按了哟position=" + position, 1).show();
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
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);
                    }

                    //预览大图加载 可以在这里做加载动画等
                    @Override
                    public <T> void loadPreImage(Context context, ImageView imageView, T object) {
                        //实现方式 glide自带监听 4.x 实现方式更加 或者其他自带监听进度的框架,这里只是demo演示用
                        final WaitDialog waitDialog = new WaitDialog(context);
                        waitDialog.show();
                        Glide.with(context).load(object.toString())
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

 





 




