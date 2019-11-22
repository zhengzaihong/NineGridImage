package com.dz.ninegridimages.interfaces;

import android.content.Context;
import android.widget.ImageView;

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * creat_date: 2019/1/22 0022
 * creat_time: 17:04
 * describe: 单张图片加载接口
 **/

public interface SinglePicImageLoader {
    /**
     * 需要子类实现该方法，以确定如何加载和显示图片
     *
     * @param context   上下文
     * @param transition 过渡展示图片的ImageView
     * @param targetImageView 需要展示图片的ImageView
     * @param object    实体类
     */

    <T> void loadPreImage(Context context, ImageView transition,ImageView targetImageView, T object);

    <T> void longClick(Context context, ImageView targetImageView, T object);



}
