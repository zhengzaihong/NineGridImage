package com.dz.ninegridimages.config;

import android.graphics.Color;

import com.dz.ninegridimages.interfaces.SinglePicImageLoader;

/**
 * creat_user: zhengzaihong
 * Email:1096877329@qq.com
 * creat_date: 2019/11/22
 * creat_time: 15:05
 * describe 配置简易的单张大图预览
 **/
public class SinglePicturePreConfig {

    //全局的图片加载器(必须设置,否者不显示图片)
    private SinglePicImageLoader imageLoader;

    //预览背景色
    private int preBgColor = Color.BLACK;


    public SinglePicImageLoader getImageLoader() {
        return imageLoader;
    }

    public int getPreBgColor() {
        return preBgColor;
    }

    public SinglePicturePreConfig setPreBgColor(int preBgColor) {
        this.preBgColor = preBgColor;
        return this;
    }


    public SinglePicturePreConfig setImageLoader(SinglePicImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }


}
