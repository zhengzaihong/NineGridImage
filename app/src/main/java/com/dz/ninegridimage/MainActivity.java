package com.dz.ninegridimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dz.ninegridimage.bean.GoodsImage;
import com.dz.ninegridimages.config.NineGridViewConfigure;
import com.dz.ninegridimages.interfaces.ImageLoader;
import com.dz.ninegridimages.interfaces.PreImageOnLongClickListener;
import com.dz.ninegridimages.view.NineGridView;
import com.dz.utlis.ClassTools;
import com.dz.utlis.ToastTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dz.solc.viewtool.adapter.CommonAdapter;

public class MainActivity extends AppCompatActivity {


    private Context mContext;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mContext = this;

        ListView listView = findViewById(R.id.listView);
        Button btGoSingle = findViewById(R.id.btGoSingle);

        btGoSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassTools.toAct(MainActivity.this, PreBigAcitivity.class);
            }
        });


        final List<GoodsImage> goodsImages = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            goodsImages.add(loadData());
        }


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


        listView.setAdapter(new CommonAdapter<GoodsImage>(this, R.layout.item_news, goodsImages) {
            @Override
            public void convert(ViewHolder holder, int position, GoodsImage entity) {

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
            }
        });


    }


    public GoodsImage loadData() {
        GoodsImage imageBean = new GoodsImage();
        imageBean.setDatas(randomUrl());
        return imageBean;
    }

    public List<String> randomUrl() {

        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            stringList.add(urls.get(new Random().nextInt(urls.size())));
        }
        return stringList;

    }

    static List<String> urls = new ArrayList<>();

    static {

        urls.add("http://img3.imgtn.bdimg.com/it/u=2606296522,109065689&fm=26&gp=0.jpg");
        urls.add("https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=98b063ef9f45d688bc02b4a494c37dab/4b90f603738da977625f2cf7bd51f8198718e3fe.jpg");
        urls.add("https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=4e7e3a7a8d0a19d8d403820503f882c9/34fae6cd7b899e518d7259df4fa7d933c9950d78.jpg");
        urls.add("http://img3.imgtn.bdimg.com/it/u=1848903733,421507316&fm=26&gp=0.jpg");
        urls.add("http://img2.imgtn.bdimg.com/it/u=3191362215,2691745071&fm=26&gp=0.jpg");
        urls.add("http://img4.imgtn.bdimg.com/it/u=3575515023,3250489571&fm=26&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=2810361144,3329888699&fm=26&gp=0.jpg");
        urls.add("http://img5.imgtn.bdimg.com/it/u=3853562523,3017244426&fm=26&gp=0.jpg");
        urls.add("http://img5.imgtn.bdimg.com/it/u=2528583926,2971147999&fm=26&gp=0.jpg");
        urls.add("http://img2.imgtn.bdimg.com/it/u=3796196028,3741032274&fm=26&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=1464879870,3180876389&fm=26&gp=0.jpg");
        urls.add("http://img4.imgtn.bdimg.com/it/u=471176057,2814422093&fm=26&gp=0.jpg");

        urls.add("http://img2.imgtn.bdimg.com/it/u=3195699516,585177818&fm=26&gp=0.jpg");
        urls.add("http://img2.imgtn.bdimg.com/it/u=1324773888,2229494300&fm=200&gp=0.jpg");
        urls.add("http://img2.imgtn.bdimg.com/it/u=2072403978,677122262&fm=26&gp=0.jpg");
        urls.add("http://img2.imgtn.bdimg.com/it/u=4104030800,1112978188&fm=26&gp=0.jpg");
    }
}


