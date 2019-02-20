package com.dz.ninegridimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dz.ninegridimage.bean.GoodsImage;
import com.dz.ninegridimages.adapter.NineGridViewAdapter;
import com.dz.ninegridimages.bean.BaseImageBean;
import com.dz.ninegridimages.config.NineGridViewConfigure;
import com.dz.ninegridimages.interfaces.ImageLoader;
import com.dz.ninegridimages.interfaces.PreImageOnLongClickListener;
import com.dz.ninegridimages.view.NineGridView;

import java.util.ArrayList;
import java.util.List;

import dz.solc.viewtool.adapter.CommonAdapter;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        final List<BaseImageBean> goodsImages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BaseImageBean imageBean = new BaseImageBean();
            List<String> urls = new ArrayList<>();
            urls.add("http://img3.imgtn.bdimg.com/it/u=2606296522,109065689&fm=26&gp=0.jpg");
            urls.add("https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=98b063ef9f45d688bc02b4a494c37dab/4b90f603738da977625f2cf7bd51f8198718e3fe.jpg");
            urls.add("https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=4e7e3a7a8d0a19d8d403820503f882c9/34fae6cd7b899e518d7259df4fa7d933c9950d78.jpg");
            urls.add("http://img3.imgtn.bdimg.com/it/u=1848903733,421507316&fm=26&gp=0.jpg");
            urls.add("http://img2.imgtn.bdimg.com/it/u=3191362215,2691745071&fm=26&gp=0.jpg");
            urls.add("http://img4.imgtn.bdimg.com/it/u=3575515023,3250489571&fm=26&gp=0.jpg");
            imageBean.setDatas(urls);
            goodsImages.add(imageBean);
        }

        final NineGridViewConfigure configure = NineGridViewConfigure.getNineGridViewConfigure()
                .setSingleImageSize(250)//设置单张图片固定宽高
                .setSingleFixed(false)//设置单张图片固定宽高
                .setRectAdius(10)//设置宫格视图图片圆角度数
                .setColumnNum(3)//设置宫格视图列数
                .setMaxImageSize(9)//设置最大显示多少张
                .setGridSpacing(10)//设置宫格视图的间距
                .setMode(NineGridView.MODE_FILL) //设置图片布局模式
                .setSingleImageRatio(1.0f)//设置单张图片的缩放比例
                .setEnablePre(true)//是否开启预览
                .setMoreTextColor(this.getResources().getColor(R.color.amber_200))//设置超过最大张数显示的文本颜色
                .setMoreTextSize(40)//设置超过最大张数显示的字体大小
                .setPreBgColor(this.getResources().getColor(R.color.amber_200)) //设置预览时的背景
                .setIndicatorMargin(10) //设置指示器间距
                .setPreTipColor(this.getResources().getColor(R.color.red)) //设置setIndicator 优先级更高
                //设置自定义指示器
                //  .setIndicator(new int[]{R.drawable.nine_view_indicator_select_dot, R.drawable.nine_view_indicator__un_select_dot})
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
                    public <T> void onDisplayImage(Context context, ImageView imageView, T object) {
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
                        //实现方式 glide自带监听 4.x 实现方式更加 或者其他自带监听进度的框架
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


        listView.setAdapter(new CommonAdapter<BaseImageBean>(this, R.layout.item_news, goodsImages) {
            @Override
            public void convert(ViewHolder holder, int position, BaseImageBean entity) {
                ((NineGridView) holder.getView(R.id.nineGrid))
                        .bindConfigure(configure)
                        .setNineAdapter(new NineGridViewAdapter(MainActivity.this, entity.datas));
            }
        });
    }
}
