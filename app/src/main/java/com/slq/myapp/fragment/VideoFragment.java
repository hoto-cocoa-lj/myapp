package com.slq.myapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.slq.myapp.R;
import com.slq.myapp.activity.LoginActivity;
import com.slq.myapp.adapter.HomeVideoAdapter;
import com.slq.myapp.api.ApiConfig;
import com.slq.myapp.entity.HomeVideoEntity;
import com.slq.myapp.entity.VideoListResponse;
import com.slq.myapp.listener.OnItemChildClickListener;
import com.slq.myapp.api.Api;
import com.slq.myapp.util.MyappStringUtils;
import com.slq.myapp.util.Tag;
import com.slq.myapp.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


@SuppressLint("ValidFragment")
public class VideoFragment extends BaseFragment implements OnItemChildClickListener {
    private int categoryId;
    Context mContext;
    int pageNum = 1;
    /*------------>*/
    private RecyclerView recyclerView;
    private HomeVideoAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    RefreshLayout refreshLayout;
    private List<HomeVideoEntity> data = new ArrayList<HomeVideoEntity>();

    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;
    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    Handler h = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;

    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new com.dueeeke.videoplayer.player.VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == com.dueeeke.videoplayer.player.VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }


    /*<------------*/
    public static VideoFragment newInstance(int categoryId, Context mContext) {
        VideoFragment sf = new VideoFragment();
        sf.categoryId = categoryId;
        sf.mContext = mContext;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    int initLayout() {
        return R.layout.fr_simple_card;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    void initView() {
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        recyclerView = mRootView.findViewById(R.id.my_recycler_view1);
        initVideoView();
    }

    @Override
    void initData() {
//        super.onViewCreated(view, savedInstanceState);
        mAdapter = new HomeVideoAdapter(data, getContext());
        mAdapter.setItemChildClickListener(this);
        mAdapter.setMDataset(data);

        recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });
        init3();
    }

    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        HomeVideoEntity videoEntity = data.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoEntity.getPlayurl());
        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = layoutManager.findViewByPosition(position);
        if (itemView == null) return;
        HomeVideoAdapter.MyViewHolder viewHolder = (HomeVideoAdapter.MyViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    void init3() {
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getContext()));
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageNum = 1;
                //refreshLayout.finishRefresh(true/*,false*/);//传入false表示刷新失败
                //getVideoList(true);
                getVideoListByCategoryId(true, categoryId);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                pageNum++;
                //refreshLayout.finishLoadMore(true/*,false*/);//传入false表示加载失败
                //getVideoList(false);
                getVideoListByCategoryId(false, categoryId);
            }
        });
        //getVideoList(true);
        getVideoListByCategoryId(true, categoryId);
    }

    /*<------------*/
    void getVideoList(boolean isRefresh) {
        String token = getStringFromSp("token");
        String url = ApiConfig.HOME_VIDEO_LIST;
        if (!MyappStringUtils.isEmpty(token)) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("limit", ApiConfig.PAGE_SIZE);
            map.put("page", pageNum);
            url = Api.appendUrl(ApiConfig.HOME_VIDEO_LIST, map);
        } else {
            showToastAsny("请先登录");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            navigateTo(LoginActivity.class);
        }
        Api.getRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("", e.getMessage());
                if (isRefresh) {
                    refreshLayout.finishRefresh(true/*,false*/);//传入false表示刷新失败
                } else {
                    refreshLayout.finishLoadMore(true/*,false*/);//传入false表示加载失败
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true/*,false*/);//传入false表示刷新失败
                } else {
                    refreshLayout.finishLoadMore(true/*,false*/);//传入false表示加载失败
                }
                String s = response.body().string();
                Gson g = new Gson();
                VideoListResponse videoListResponse = g.fromJson(s, VideoListResponse.class);
                if (videoListResponse != null && videoListResponse.getCode() == 0) {
                    List<HomeVideoEntity> list = videoListResponse.getPage().getList();
                    if (list != null && !list.isEmpty()) {
                        if (isRefresh) {
                            data.clear();
                            data.addAll(list);
                        } else {
                            data.addAll(list);
                        }
                        getActivity().runOnUiThread(new Thread() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        if (isRefresh) {
                            showToastAsny("没有任何数据");
                        } else {
                            showToastAsny("没有新数据了");
                        }
                    }
                }
            }
        });
    }

    void getVideoListByCategoryId(boolean isRefresh, int categoryId) {
        String token = getStringFromSp("token");
        String url = ApiConfig.HOME_VIDEO_LIST;
        if (!MyappStringUtils.isEmpty(token)) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("limit", 99);
            map.put("page", 1);
            url = Api.appendUrl(ApiConfig.HOME_VIDEO_LIST, map);
        } else {
            showToastAsny("请先登录");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            navigateTo(LoginActivity.class);
        }
        Api.getRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("", e.getMessage());
                if (isRefresh) {
                    refreshLayout.finishRefresh(true/*,false*/);//传入false表示刷新失败
                } else {
                    refreshLayout.finishLoadMore(true/*,false*/);//传入false表示加载失败
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true/*,false*/);//传入false表示刷新失败
                } else {
                    refreshLayout.finishLoadMore(true/*,false*/);//传入false表示加载失败
                }
                String s = response.body().string();
                Gson g = new Gson();
                VideoListResponse videoListResponse = g.fromJson(s, VideoListResponse.class);
                if (videoListResponse != null && videoListResponse.getCode() == 0) {
                    List<HomeVideoEntity> list1 = videoListResponse.getPage().getList();
                    List<HomeVideoEntity> list = list1.stream().filter(new Predicate<HomeVideoEntity>() {
                        @Override
                        public boolean test(HomeVideoEntity homeVideoEntity) {
                            if (homeVideoEntity.getCategoryId() == categoryId) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }).collect(Collectors.toList());
                    if (list != null && !list.isEmpty()) {
                        if (isRefresh) {
                            data.clear();
                            data.addAll(list);
                        } else {
                            data.addAll(list);
                        }
//                        getActivity().runOnUiThread(new Thread() {
//                            @Override
//                            public void run() {
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        });
                        h.sendEmptyMessage(0);
                    } else {
                        if (isRefresh) {
                            showToastAsny("没有任何数据");
                        } else {
                            showToastAsny("没有新数据了");
                        }
                    }
                }
            }
        });
    }
}