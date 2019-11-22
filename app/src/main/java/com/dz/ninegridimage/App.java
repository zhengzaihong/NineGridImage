package com.dz.ninegridimage;

import android.app.Application;
import android.graphics.Color;

import com.dz.utlis.ScreenUtils;
import com.dz.utlis.ToastTool;
import com.dz.utlis.UiCompat;
import com.dz.utlis.view.ToastConfig;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Toast 配置
        ToastConfig config = new ToastConfig()
            .setInterval(2000)
            .setRadiusBg((int) ScreenUtils.dip2px(this,30))
            .setToastTextColor(UiCompat.getColor(getResources(),R.color.light_blue_200))
            .setToastViewGroupBgColor(Color.WHITE)
            .setToastTextSize(16)
            .setBgPadding((int) ScreenUtils.dip2px(this,15))
            .setShortToast(false)
            .setStrokeWidth(0)
            .setRadiusType(ToastConfig.RadiusType.ALL_RADIUS)
            .setStrokeColor(Color.GREEN);

        //初始化 Toast工具
        ToastTool.get().initConfig(this,config);
    }
}
