package com.dz.ninegridimages.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dz.ninegridimages.config.SinglePicturePreConfig;
import com.dz.ninegridimages.preview.BigPicturePreviewActivity;

/**
 * creat_user: zhengzaihong
 * Email:1096877329@qq.com
 * creat_date: 2019/11/22
 * creat_time: 14:26
 * describe   拓展到 非九宫格 预览大图也支持
 *
 **/
public class SingleViewPreViewHelper {

    private static SingleViewPreViewHelper viewHelper;

    private SinglePicturePreConfig singlePicturePreConfig = new SinglePicturePreConfig();

    public static SingleViewPreViewHelper getInstance() {

        if (null == viewHelper) {
            synchronized (SingleViewPreViewHelper.class) {

                if (null == viewHelper) {
                    viewHelper = new SingleViewPreViewHelper();
                }
            }
        }
        return viewHelper;
    }

    public static SingleViewPreViewHelper getViewHelper() {
        return viewHelper;
    }

    public static void setViewHelper(SingleViewPreViewHelper viewHelper) {
        SingleViewPreViewHelper.viewHelper = viewHelper;
    }

    public SinglePicturePreConfig getSinglePicturePreConfig() {
        return singlePicturePreConfig;
    }

    public void setSinglePicturePreConfig(SinglePicturePreConfig singlePicturePreConfig) {
        this.singlePicturePreConfig = singlePicturePreConfig;
    }

    public void toActClearTop(Context mContext, Bundle bundle) {

        Intent intent = new Intent(mContext, BigPicturePreviewActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
