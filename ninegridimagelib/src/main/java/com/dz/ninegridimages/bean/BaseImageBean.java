package com.dz.ninegridimages.bean;

import java.io.Serializable;
import java.util.List;

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * creat_date: 2019/1/20 0020
 * creat_time: 19:35
 * describe: 设置进来图片对象的必须是该类的子类
 **/

public class BaseImageBean<T> implements Serializable {

    private String imageUrl;
    private int imageId;

    public int imageViewHeight;
    public int imageViewWidth;
    public int imageViewX;
    public int imageViewY;
    public List<T> datas;

    public BaseImageBean() {
    }

    public BaseImageBean(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BaseImageBean(int imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageViewHeight() {
        return imageViewHeight;
    }

    public void setImageViewHeight(int imageViewHeight) {
        this.imageViewHeight = imageViewHeight;
    }

    public int getImageViewWidth() {
        return imageViewWidth;
    }

    public void setImageViewWidth(int imageViewWidth) {
        this.imageViewWidth = imageViewWidth;
    }

    public int getImageViewX() {
        return imageViewX;
    }

    public void setImageViewX(int imageViewX) {
        this.imageViewX = imageViewX;
    }

    public int getImageViewY() {
        return imageViewY;
    }

    public void setImageViewY(int imageViewY) {
        this.imageViewY = imageViewY;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "BaseImageBean{" +
                "imageUrl='" + imageUrl + '\'' +
                ", imageId=" + imageId +
                ", imageViewHeight=" + imageViewHeight +
                ", imageViewWidth=" + imageViewWidth +
                ", imageViewX=" + imageViewX +
                ", imageViewY=" + imageViewY +
                ", datas=" + datas +
                '}';
    }
}
