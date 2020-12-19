package com.slq.myapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import androidx.appcompat.app.AppCompatDelegate ;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;
import androidx.annotation.NonNull;

import com.slq.myapp.MainActivity;


public class BaseActivity extends AppCompatActivity {
    public Context mContext;
    //教程里initLayout返回该layout的id值，似乎没用，可以不要
    int initLayout(){return 0;};
    //教程里initView用findviewbyid来给private view初始化，这里用Bindview所以不需要
    void initView(){};
    //各自初始化
    void initData(){};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("","name----->"+getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        mContext=this;
        initLayout();
        initView();
        initData();

    }
    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

    public void showToast(String msg){
        //Log.e("tttt", this.toString());
        //Log.e("tttt", mContext.toString());       //mContext==this
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }
    public void showToastAsny(String msg){
        Looper.prepare();
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class<?> cls){
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }
    public void navigateToWithFlag(Class<?> cls,int flag){
        Intent intent = new Intent(mContext, cls);
        intent.setFlags(flag);
        startActivity(intent);
    }
    public void navigateToWithBundle(Class<?> cls,Bundle bundle){
        Intent intent = new Intent(mContext, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void saveString2Sp(String key,String val){
        SharedPreferences sp=getSharedPreferences("slq",MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,val);
        edit.commit();
    }
    public String getStringFromSp(String key) {
        SharedPreferences sp =getSharedPreferences("slq", MODE_PRIVATE);
        return sp.getString(key, "");
    }

}
