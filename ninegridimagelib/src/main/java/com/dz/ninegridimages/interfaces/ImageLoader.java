package com.dz.ninegridimages.interfaces;

import android.content.Context;
import android.widget.ImageView;

/**
 *creat_user: zhengzaihong
 *email:1096877329@qq.com
 *creat_date: 2019/1/22 0022
 *creat_time: 17:04
 *describe: 图片加载接口
 **/

public interface ImageLoader <T> {
    /**
     * 需要子类实现该方法，以确定如何加载和显示图片
     *
     * @param context   上下文
     * @param imageView 需要展示图片的ImageView
     * @param object    实体类
     */
    void onDisplayImage(Context context, ImageView imageView, T object);


    void loadPreImage(Context context, ImageView imageView, T object);

}
