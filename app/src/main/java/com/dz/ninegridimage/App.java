package com.dz.ninegridimage;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.dz.utlis.ScreenUtils;
import com.dz.utlis.ToastTool;
import com.dz.utlis.UiCompat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dz.utlis.JavaUtils.isdebug;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        isdebug = true;
        // Toast 配置
        ToastTool.options()
                .setInterval(2000)
                .setRadius((int) ScreenUtils.dip2px(this, 30))
                .setTextColor(UiCompat.getColor(getResources(), R.color.light_blue_200))
                .setBackGroundColor(Color.WHITE)
                .setTextSize(16)
                .setPadding((int) ScreenUtils.dip2px(this, 15))
                .setLongTime(false)
                .setStrokeWidth(0)
                .setRadiusType(ToastTool.RadiusType.ALL_RADIUS)
                .setStrokeColor(Color.GREEN)
                .build(this);


    }
}
