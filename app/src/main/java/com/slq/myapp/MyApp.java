package com.slq.myapp;

import android.app.Application;
import android.content.SharedPreferences;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
                //.addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                //.addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                //.addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
        SharedPreferences slq = getSharedPreferences("slq", MODE_PRIVATE);
        String skin = slq.getString("skin", "default");
        if (!skin.equals("night")) {
            // 恢复应用默认皮肤
            SkinCompatManager.getInstance().restoreDefaultTheme();
        } else {
            SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
        }

    }
}
