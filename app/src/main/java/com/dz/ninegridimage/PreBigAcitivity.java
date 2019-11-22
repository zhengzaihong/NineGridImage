package com.dz.ninegridimage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dz.ninegridimages.config.SinglePicturePreConfig;
import com.dz.ninegridimages.interfaces.SinglePicImageLoader;
import com.dz.ninegridimages.util.SingleViewPreViewHelper;
import com.dz.utlis.ToastTool;
import com.dz.utlis.UiCompat;

public class PreBigAcitivity extends AppCompatActivity {

    private String url = "http://img3.imgtn.bdimg.com/it/u=2606296522,109065689&fm=26&gp=0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_bgimage);


        ImageView imageView = findViewById(R.id.imageTest);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("url", url);

                SingleViewPreViewHelper.getInstance().toActClearTop(PreBigAcitivity.this, bundle);

            }
        });


        Glide.with(this).load(url)
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_default_color)
                .override(100, 100)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);


        final SinglePicturePreConfig configure = new SinglePicturePreConfig()
                //设置大图浏览背景
                .setPreBgColor(UiCompat.getColor(getResources(), R.color.yellow_200))
                //设置图片加载器（必须设置） 上面的都是非必须
                .setImageLoader(new SinglePicImageLoader() {
                    @Override
                    public <T> void longClick(Context context, ImageView targetImageView, T object) {

                        ToastTool.get().show("你长按了哟");
                    }

                    //预览大图加载 可以在这里做加载动画等
                    @Override
                    public <T> void loadPreImage(Context context, final ImageView transition, final ImageView imageView, T object) {
                        //实现方式 glide自带监听 4.x 实现方式更加 或者其他自带监听进度的框架

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


    }
}
