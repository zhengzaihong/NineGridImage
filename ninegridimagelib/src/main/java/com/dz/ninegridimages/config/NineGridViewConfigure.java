package com.dz.ninegridimages.config;


import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.dz.ninegridimages.R;
import com.dz.ninegridimages.interfaces.ImageLoader;
import com.dz.ninegridimages.interfaces.PreImageOnLongClickListener;
import com.dz.ninegridimages.view.NineGridView;

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * creat_date: 2019/1/22 0022
 * creat_time: 14:47
 * describe: 宫格视图参数配置
 **/


public class NineGridViewConfigure {

    private static NineGridViewConfigure nineGridViewConfigure;
    private static PreImageOnLongClickListener longClickListener;

    //设置九宫格图片圆角度数
    private int rectAdius = 0;
    //设置指示器drawable id
    private int[] indicator;
    //文本指示器字体颜色
    private int preTipColor = Color.WHITE;
    //预览背景色
    private int preBgColor = Color.BLACK;
    //指示器边距
    private int indicatorMargin = 8;
    //设置列数
    private int columnNum = 3;
    //全局的图片加载器(必须设置,否者不显示图片)
    private ImageLoader imageLoader;
    // 单张图片时的最大大小,单位px
    private int singleImageSize = 250;
    //单张照片时是否固定宽高
    private boolean singleFixed = false;
    // 单张图片的宽高比(宽/高)
    private float singleImageRatio = 1.0f;
    // 最大显示的图片数
    private int maxImageSize = 9;
    // 宫格间距，单位px
    private int gridSpacing = 3;
    // 默认使用fill模式
    private int mode = NineGridView.MODE_FILL;
    //显示文字的大小单位sp
    private float moreTextSize = 35;
    //显示文字的颜色
    private int moreTextColor = 0xFFFFFFFF;
    //设置指示器字体大小
    private int preTipTextSize = 22;


    public static NineGridViewConfigure getNineGridViewConfigure() {

        if (null == nineGridViewConfigure) {
            synchronized (NineGridViewConfigure.class) {
                if (null == nineGridViewConfigure) {

                    nineGridViewConfigure = new NineGridViewConfigure();
                }
            }
        }
        return nineGridViewConfigure;
    }

    private NineGridViewConfigure() {
    }


    public NineGridViewConfigure setOnPreLongClickListener(PreImageOnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        return this;
    }

    public float getMoreTextSize() {
        return moreTextSize;
    }

    public NineGridViewConfigure setMoreTextSize(float textSize) {
        this.moreTextSize = textSize;
        return this;
    }

    public int getMoreTextColor() {
        return moreTextColor;
    }

    public NineGridViewConfigure setMoreTextColor(int textColor) {
        this.moreTextColor = textColor;
        return this;
    }

    public int getPreTipTextSize() {
        return preTipTextSize;
    }

    public NineGridViewConfigure setPreTipTextSize(int preTipTextSize) {
        this.preTipTextSize = preTipTextSize;
        return this;
    }


    public NineGridViewConfigure setPreTipColor(int preTipColor) {
        this.preTipColor = preTipColor;
        return this;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public NineGridViewConfigure setColumnNum(int columnNum) {
        this.columnNum = columnNum;
        return this;
    }

    public int getSingleImageSize() {
        return singleImageSize;
    }

    public NineGridViewConfigure setSingleImageSize(int singleImageSize) {
        this.singleImageSize = singleImageSize;
        return this;
    }

    public boolean isSingleFixed() {
        return singleFixed;
    }

    public NineGridViewConfigure setSingleFixed(boolean singleFixed) {
        this.singleFixed = singleFixed;
        return this;
    }

    public float getSingleImageRatio() {
        return singleImageRatio;
    }

    public NineGridViewConfigure setSingleImageRatio(float singleImageRatio) {
        this.singleImageRatio = singleImageRatio;
        return this;
    }

    public int getMaxImageSize() {
        return maxImageSize;
    }

    public NineGridViewConfigure setMaxImageSize(int maxImageSize) {
        this.maxImageSize = maxImageSize;
        return this;
    }

    public int getGridSpacing() {
        return gridSpacing;
    }

    public NineGridViewConfigure setGridSpacing(int gridSpacing) {
        this.gridSpacing = gridSpacing;
        return this;
    }

    public int getMode() {
        return mode;
    }

    public NineGridViewConfigure setMode(int mode) {
        this.mode = mode;
        return this;
    }

    public PreImageOnLongClickListener getOnPreLongClickListener() {
        return longClickListener;
    }

    public int getRectAdius() {
        return rectAdius;
    }

    public int[] getIndicator() {
        return indicator;
    }

    public int getPreTipColor() {
        return preTipColor;
    }

    public int getIndicatorMargin() {
        return indicatorMargin;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public int getPreBgColor() {
        return preBgColor;
    }

    public NineGridViewConfigure setPreBgColor(int preBgColor) {
        this.preBgColor = preBgColor;
        return this;
    }

    public NineGridViewConfigure setRectAdius(int rectAdius) {
        this.rectAdius = rectAdius;
        return this;
    }

    public NineGridViewConfigure setIndicator(int[] indicator) {
        this.indicator = indicator;
        return this;
    }


    public NineGridViewConfigure setIndicatorMargin(int margin) {
        this.indicatorMargin = margin;
        return this;
    }

    public NineGridViewConfigure setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

}
