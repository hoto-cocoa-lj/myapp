package com.slq.myapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.slq.myapp.R;
import com.slq.myapp.activity.LoginActivity;
import com.slq.myapp.activity.WebActivity;
import com.slq.myapp.adapter.HomeVideoAdapter;
import com.slq.myapp.adapter.NewsAdapter;
import com.slq.myapp.api.Api;
import com.slq.myapp.api.ApiConfig;
import com.slq.myapp.entity.HomeVideoEntity;
import com.slq.myapp.entity.NewsEntity;
import com.slq.myapp.entity.NewsListResponse;
import com.slq.myapp.listener.OnObjectItemClickListener;
import com.slq.myapp.util.MyappStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends BaseFragment {
    RecyclerView recyclerView;
    int pageNum = 1;
    private NewsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    RefreshLayout refreshLayout;
    private List<NewsEntity> data = new ArrayList<NewsEntity>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    void initView() {
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        recyclerView = mRootView.findViewById(R.id.news_recycler_view);
        mAdapter = new NewsAdapter(data, getContext());
        mAdapter.setOnClickListener1(new OnObjectItemClickListener() {
            @Override
            public void onItemClick(Object entity) {
                NewsEntity newsEntity=(NewsEntity)entity;
                String newsTitle = newsEntity.getNewsTitle();
                String url="https://m.vip.com/product-1710613157-6918041719313096901.html";
                Bundle bundle = new Bundle();
                bundle.putString("url",url);
                navigateToWithBundle(WebActivity.class,bundle);
            }

        });
        mAdapter.setOnClickListener2(new OnObjectItemClickListener() {
            @Override
            public void onItemClick(Object entity) {
                NewsEntity newsEntity=(NewsEntity)entity;
                String newsTitle = newsEntity.getNewsTitle();
                String url="http://www.qq.com";
                Bundle bundle = new Bundle();
                bundle.putString("url",url);
                navigateToWithBundle(WebActivity.class,bundle);
            }
        });
        //mAdapter.setData(data);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);
        initHeadAndFooter();
    }

    void initHeadAndFooter() {
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getContext()));
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageNum = 1;
                getNewsList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                pageNum++;
                getNewsList(false);
            }
        });
    }

    @Override
    void initData() {
        getNewsList(true);
    }

    private void getNewsList(boolean isRefresh) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", getStringFromSp("token"));
        map.put("limit", ApiConfig.PAGE_SIZE);
        map.put("page", pageNum);
        Map<String, Object> headerMap = new HashMap<>();
        //headerMap.put("token", getStringFromSp("token"));
        String url = Api.appendUrl(ApiConfig.NEWS_LIST, map);
        Api.getRequest(url,headerMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToastAsny(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true/*,false*/);//传入false表示刷新失败
                } else {
                    refreshLayout.finishLoadMore(true/*,false*/);//传入false表示加载失败
                }
                String s = response.body().string();
                NewsListResponse newsListResponse = new Gson().fromJson(s, NewsListResponse.class);
                if (newsListResponse != null && newsListResponse.getCode() == 0) {
                    NewsListResponse.PageBean page = newsListResponse.getPage();
                    if (page != null) {
                        List<NewsEntity> list = page.getList();
                        if (list != null && !list.isEmpty()) {
                            if (isRefresh) {
                                data.clear();
                                data.addAll(list);
                            } else {
                                data.addAll(list);
                            }
                            handler.sendEmptyMessage(0);
                            //RecyclerView.LayoutManager layoutManager1 = recyclerView.getLayoutManager();
                            return;
                        }
                    }
                    if (isRefresh) {
                        showToastAsny("没有任何资讯数据");
                    } else {
                        showToastAsny("没有新的资讯数据");
                    }
                } else {
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1111);
                                navigateTo(LoginActivity.class);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    showToastAsny(newsListResponse.getMsg());
                }
            }
        });
    }

    public NewsFragment() {
        // Required empty public constructor
    }


    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}