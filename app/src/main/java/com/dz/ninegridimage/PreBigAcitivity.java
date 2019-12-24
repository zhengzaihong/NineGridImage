package com.dz.ninegridimage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dz.ninegridimages.config.NineGridViewConfigure;
import com.dz.ninegridimages.interfaces.ImageLoader;
import com.dz.ninegridimages.interfaces.PreImageOnLongClickListener;
import com.dz.ninegridimages.util.NineGridViewHelper;
import com.dz.utlis.ToastTool;

import java.util.ArrayList;
import java.util.List;


/**
 * creat_user: zhengzaihong
 * Email:1096877329@qq.com
 * creat_date: 2019/12/24
 * creat_time: 9:35
 * describe 单独显示大图
 **/
public class PreBigAcitivity extends AppCompatActivity {

    List<String> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_bgimage);


        urls.add("http://img3.imgtn.bdimg.com/it/u=2606296522,109065689&fm=26&gp=0.jpg");
        urls.add("https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=98b063ef9f45d688bc02b4a494c37dab/4b90f603738da977625f2cf7bd51f8198718e3fe.jpg");
        urls.add("https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=4e7e3a7a8d0a19d8d403820503f882c9/34fae6cd7b899e518d7259df4fa7d933c9950d78.jpg");
        urls.add("http://img3.imgtn.bdimg.com/it/u=1848903733,421507316&fm=26&gp=0.jpg");
        urls.add("http://img2.imgtn.bdimg.com/it/u=3191362215,2691745071&fm=26&gp=0.jpg");


        ImageView imageView = findViewById(R.id.imageTest);

        Glide.with(this).load(urls.get(0))
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_default_color)
                .override(100, 100)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);


        final NineGridViewConfigure configure = new NineGridViewConfigure()
                .setEnablePre(true)//是否开启预览
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


        // 这里只是单子模拟，真实开发往往是一个列表显示图片集合，点击某个显示大图
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NineGridViewHelper.getInstance().startPreBigImage(PreBigAcitivity.this, configure, 0, urls);

            }
        });


    }
}
