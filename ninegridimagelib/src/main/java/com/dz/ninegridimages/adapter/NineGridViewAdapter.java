package com.dz.ninegridimages.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dz.ninegridimages.R;
import com.dz.ninegridimages.bean.BaseImageBean;
import com.dz.ninegridimages.preview.ImagePreviewActivity;
import com.dz.ninegridimages.util.CopyUtil;
import com.dz.ninegridimages.view.NineGridView;
import com.dz.ninegridimages.view.NineGridViewWrapper;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * creat_date: 2019/1/22 0022
 * creat_time: 16:34
 * describe: 宫格视图适配器
 **/
public class NineGridViewAdapter<T> {

    private List<T> listImage;
    private int statusHeight;

    public NineGridViewAdapter(Context mContext, List<T> listImage) {
        this.listImage = listImage;
        this.statusHeight = getStatusHeight(mContext);
    }


    public List<T> getImage() {
        return listImage;
    }

    /**
     * 如果要实现图片点击的逻辑，重写此方法即可
     * @param context      上下文
     * @param nineGridView 九宫格控件
     * @param index        当前点击图片的的索引
     * @param imageInfo    图片地址的数据集合
     */
    public void onImageItemClick(Context context, NineGridView nineGridView, int index, List<T> imageInfo) {
        if (!nineGridView.enablePre) {
            return;
        }
        BaseImageBean imageBean = new BaseImageBean();
        for (int i = 0; i < imageInfo.size(); i++) {
            try {
                View imageView;
                if (i < nineGridView.getMaxSize()) {
                    imageView = nineGridView.getChildAt(i);
                } else {
                    //如果图片的数量大于显示的数量，则超过部分的返回动画统一退回到最后一个图片的位置
                    imageView = nineGridView.getChildAt(nineGridView.getMaxSize() - 1);
                }
                imageBean.imageViewWidth = imageView.getWidth();
                imageBean.imageViewHeight = imageView.getHeight();
                int[] points = new int[2];
                imageView.getLocationInWindow(points);
                imageBean.imageViewX = points[0];
                imageBean.imageViewY = points[1] - statusHeight;
                imageBean.setDatas(imageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(context, ImagePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, imageBean);
        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);

    }

    /**
     * 生成ImageView容器的方式，默认使用NineGridImageViewWrapper类，即点击图片后，图片会有蒙板效果
     * 如果需要自定义图片展示效果，重写此方法即可
     *
     * @param context 上下文
     * @return 生成的 ImageView
     */
    public ImageView generateImageView(Context context) {
        NineGridViewWrapper imageView = new NineGridViewWrapper(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.ic_default_color);
        return imageView;
    }

    /**
     * 获得状态栏的高度
     */
    public int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
