package com.slq.myapp.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.slq.myapp.R;
import com.slq.myapp.adapter.MyPagerAdapter;
import com.slq.myapp.entity.TabEntity;
import com.slq.myapp.fragment.HomeFragment;
import com.slq.myapp.fragment.MyFragment;
import com.slq.myapp.fragment.NewsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends BaseActivity{
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private int initPage = 2;
    private String[] mTitles = {"首页", "资讯", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_more_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @BindView(R.id.viewpager)
    //FixedViewPager mViewPager;    //layout里使用FixedViewPager，这里可以继续用ViewPager
    ViewPager mViewPager;
    @BindView(R.id.commomTabLayout)
    CommonTabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        //必须用这个顺序，否则报错空指针
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    void initData() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = HomeFragment.newInstance(this);
        mFragments.add(homeFragment);
        mFragments.add(NewsFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mViewPager.setAdapter(new MyPagerAdapter(supportFragmentManager, mFragments, mTitles));
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
                //源码里已经setCurrentTab，所以只需要修改viewpager的fragment
                //mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(initPage);
        //setOffscreenPageLimit默认值1，表示当前页面的左右两边各保留1个页面，其他页面会销毁，
        //需要加载时重新创建。频繁切换可能报错下标越界。
        // 设置成mFragments.size()-1，保证所有页面都不会销毁。
        mViewPager.setOffscreenPageLimit(mFragments.size()-1);
        mTabLayout.setCurrentTab(initPage);
    }


}