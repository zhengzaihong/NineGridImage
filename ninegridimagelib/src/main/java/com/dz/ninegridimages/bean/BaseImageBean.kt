package com.dz.ninegridimages.bean

import java.io.Serializable

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * create_date: 2019/1/20 0020
 * create_time: 19:35
 * describe: 设置进来图片对象的必须是该类的子类
 */
open class BaseImageBean<T> : Serializable {
    
    var imageUrl = ""
    var imageId = 0
    var imageViewHeight = 0
    var imageViewWidth = 0
    var imageViewX = 0
    var imageViewY = 0
    var datas: List<T> = ArrayList()


    override fun toString(): String {
        return "BaseImageBean{" +
                "imageUrl='" + imageUrl + '\'' +
                ", imageId=" + imageId +
                ", imageViewHeight=" + imageViewHeight +
                ", imageViewWidth=" + imageViewWidth +
                ", imageViewX=" + imageViewX +
                ", imageViewY=" + imageViewY +
                ", datas=" + datas +
                '}'
    }
}