package com.dz.ninegridimages.config

import java.io.Serializable

/**
 * create_user: zhengzaihong
 * email:1096877329@qq.com
 * create_date: 2020/8/11
 * create_time: 10:37
 * describe: 指示器的边界修饰类型
 **/
enum class RadiusType:Serializable{
    /**
     * 左边_上_下圆角
     */
    LEFT_TOP_BOTTOM_RADIUS,

    /**
     * 右边_上_下圆角
     */
    RIGHT_TOP_BOOTOM_RADIUS,

    /**
     * 左边_上圆角
     */
    LEFT_TOP_RADIUS,

    /**
     * 左边_下圆角
     */
    LEFT_BOTTOM_RADIUS,

    /**
     * 右边_上圆角
     */
    RIGHT_TOP_RADIUS,

    /**
     * 右边_下圆角
     */
    RIGHT_BOOTOM_RADIUS,

    /**
     * 四边圆角
     */
    ALL_RADIUS,

    /**
     * 无圆角
     */
    NONE_RADIUS
}
