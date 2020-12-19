package com.slq.myapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dueeeke.videoplayer.player.VideoViewManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class BaseFragment extends Fragment {
    View mRootView;
    Unbinder unbinder;
    int initLayout() {
        return 0;
    }

    void initView() {
    }

    void initData() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(initLayout(), container, false);
            //initView();
        }
        unbinder= ButterKnife.bind(this,mRootView);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        initData();
    }

    public void showToast(String msg) {
        //Log.e("tttt", this.toString());
        //Log.e("tttt", mContext.toString());       //mContext==this
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToastAsny(String msg) {
        Looper.prepare();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        startActivity(intent);
    }
    public void navigateToWithBundle(Class<?> cls,Bundle bundle){
        Intent intent = new Intent(getContext(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void saveString2Sp(String key, String val) {
        SharedPreferences sp = getActivity().getSharedPreferences("slq", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, val);
        edit.commit();
    }

    public String getStringFromSp(String key) {
        SharedPreferences sp = getActivity().getSharedPreferences("slq", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public VideoViewManager getVideoViewManager(){
        return VideoViewManager.instance();
    }
}
