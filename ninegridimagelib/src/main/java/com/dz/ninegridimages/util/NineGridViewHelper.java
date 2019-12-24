package com.dz.ninegridimages.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dz.ninegridimages.bean.BaseImageBean;
import com.dz.ninegridimages.config.NineGridViewConfigure;
import com.dz.ninegridimages.preview.ImagePreviewActivity;

import java.util.List;

/**
 * creat_user: zhengzaihong
 * Email:1096877329@qq.com
 * creat_date: 2019/12/23
 * creat_time: 16:48
 * describe 宫格视图的帮助类
 **/
public class NineGridViewHelper {


    /**
     * 单例
     */
    private static NineGridViewHelper nineGridViewHelper;

    /**
     * 只记录最新一个启动大图的 配置文件
     */
    private NineGridViewConfigure nineGridViewConfigure = new NineGridViewConfigure();

    /***
     * 缓存 adapter 起到一定性能作用
     */
    private MessageQueue adapterQueue = new MessageQueue(15);

    public static NineGridViewHelper getInstance() {

        if (null == nineGridViewHelper) {
            synchronized (NineGridViewHelper.class) {

                if (null == nineGridViewHelper) {
                    nineGridViewHelper = new NineGridViewHelper();
                }
            }
        }
        return nineGridViewHelper;
    }


    /**
     * 非宫格视图 启动大图预览
     *
     * @param context   上下文
     * @param configure 大图显示的配置i文件
     * @param index     点击的第几个图片，索引
     * @param imageInfo 图片集合
     */
    public void startPreBigImage(Context context, NineGridViewConfigure configure, int index, List<?> imageInfo) {

        BaseImageBean imageBean = new BaseImageBean();
        imageBean.setDatas(imageInfo);

        setNineGridViewConfigure(configure);
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, imageBean);
        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);

    }


    public NineGridViewConfigure getNineGridViewConfigure() {
        return nineGridViewConfigure;
    }

    public void setNineGridViewConfigure(NineGridViewConfigure nineGridViewConfigure) {
        this.nineGridViewConfigure = nineGridViewConfigure;
    }

    public MessageQueue getAdapterQueue() {
        return adapterQueue;
    }

    public void setAdapterQueue(MessageQueue adapterQueue) {
        this.adapterQueue = adapterQueue;
    }
}
