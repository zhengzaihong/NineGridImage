package com.dz.ninegridimages.interfaces;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * creat_date: 2019/1/22 0022
 * creat_time: 17:04
 * describe: 图片加载接口
 **/

public interface ImageLoader extends Serializable {


    /**
     * 显示宫格视图的 监听器
     */
    interface OnNineGridImageListener extends ImageLoader{

        /**
         * 需要子类实现该方法，以确定如何加载和显示图片
         *
         * @param context   上下文
         * @param imageView 需要展示图片的ImageView
         * @param obj    实体类
         */

        <T> void displayImage(Context context, ImageView imageView, T obj);

    }


    /**
     * 加载预览大图的监听器
     */
    interface OnPreBigImageListener extends ImageLoader{

        /**
         * 需要子类实现该方法，以确定如何加载和显示图片
         *
         * @param context   上下文
         * @param imageView 需要展示图片的ImageView
         * @param obj    实体类
         */
        <E> void loadPreImage(Context context, ImageView imageView, E obj,int index);

    }


}
