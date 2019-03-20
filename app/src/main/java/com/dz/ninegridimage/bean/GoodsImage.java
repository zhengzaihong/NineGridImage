package com.dz.ninegridimage.bean;

import com.dz.ninegridimages.bean.BaseImageBean;

import java.util.List;

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * creat_date: 2019/1/20 0020
 * creat_time: 20:13
 * describe: TODO
 **/

public class GoodsImage<T> extends BaseImageBean {

    private String title;

    private String content;

    private List<T> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "GoodsImage{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", list=" + list +
                '}';
    }
}
