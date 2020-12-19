package com.slq.myapp.fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.slq.myapp.R;
import com.slq.myapp.adapter.MyPagerAdapter;
import com.slq.myapp.api.ApiConfig;
import com.slq.myapp.entity.CategoryEntity;
import com.slq.myapp.entity.VideoCategoryListResponse;
import com.slq.myapp.api.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends BaseFragment implements OnTabSelectListener {
    private Context mContext;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    //private final String[] mTitles = {"关注", "推荐", "新闻" , "游戏",  "娱乐", "综艺"};
    String[] mTitles;
    private MyPagerAdapter mAdapter;
    SlidingTabLayout tabLayout;
    ViewPager vp;
    List<CategoryEntity> list;

    Handler h = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int i = 0;
            for (CategoryEntity category : list) {
                mTitles[i++] = category.getCategoryName();
                mFragments.add(VideoFragment.newInstance(category.getCategoryId(), getActivity()));
            }
            mAdapter = new MyPagerAdapter(getParentFragmentManager(), mFragments, mTitles);
            vp.setAdapter(mAdapter);
            vp.setOffscreenPageLimit(mFragments.size() - 1);
            tabLayout.setViewPager(vp);
            tabLayout.setOnTabSelectListener(HomeFragment.this);
        }
    };

    @Override
    int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    void initView() {
        tabLayout = mRootView.findViewById(R.id.tl_10);
        vp = mRootView.findViewById(R.id.vp);
    }

    @Override
    void initData() {
        getVideoCategoryList();
    }

    @Override
    public void onTabSelect(int position) {
        //Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTabReselect(int position) {
        //Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    public HomeFragment() {
    }


    public static HomeFragment newInstance(Context mContext) {
        HomeFragment fragment = new HomeFragment();
        fragment.mContext = mContext;
        return fragment;
    }

    void getVideoCategoryList() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", getStringFromSp("token"));
        String url = Api.appendUrl(ApiConfig.HOME_VIDEO_CATEGORY_LIST, map);
        Api.getRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToastAsny(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();
                VideoCategoryListResponse categoryListResponse = gson.fromJson(body, VideoCategoryListResponse.class);
                if (categoryListResponse != null && categoryListResponse.getCode() == 0) {
                    list = categoryListResponse.getPage().getList();
                    if (list != null && !list.isEmpty()) {
                        mTitles = new String[list.size()];
                        h.sendEmptyMessage(0);
//                        getActivity().runOnUiThread(new Thread(){
//                            @Override
//                            public void run() {
//                                mAdapter = new MyPagerAdapter(getParentFragmentManager(), mFragments, mTitles);
//                                vp.setAdapter(mAdapter);
//                                vp.setOffscreenPageLimit(mFragments.size()-1);
//                                tabLayout.setViewPager(vp);
//                                tabLayout.setOnTabSelectListener(HomeFragment.this);
//                            }
//                        });
                    }
                }
            }
        });
    }
}