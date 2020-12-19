package com.slq.myapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.component.PrepareView;
import com.slq.myapp.R;
import com.slq.myapp.api.Api;
import com.slq.myapp.api.ApiConfig;
import com.slq.myapp.entity.HomeVideoEntity;
import com.slq.myapp.listener.OnItemChildClickListener;
import com.slq.myapp.listener.OnItemClickListener;
import com.slq.myapp.view.CirclePictureTransform;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
public class HomeVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnItemChildClickListener itemChildClickListener;
    private OnItemClickListener itemClickListener;
    private List<HomeVideoEntity> mDataset;
    private Context mContext;
    public HomeVideoAdapter(List<HomeVideoEntity> mDataset,Context mContext){
        this.mDataset=mDataset;
        this.mContext=mContext;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.video_one, parent, false);
        RecyclerView.ViewHolder ViewHolder = new MyViewHolder(v);
        return ViewHolder;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView userhead,collect,like;
        public TextView title,title2,video_text1,video_text2,video_text3;
        @Setter
        boolean isLike,isCollect;

        public PrepareView mPrepareView;
        public ImageView mThumb;
        public FrameLayout mPlayerContainer;
        public int mPosition;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mPrepareView=itemView.findViewById(R.id.prepare_view);
            mPlayerContainer=itemView.findViewById(R.id.player_container);
            mThumb =  mPrepareView.findViewById(R.id.thumb);

            userhead =  itemView.findViewById(R.id.video_userhead);
            collect =  itemView.findViewById(R.id.video_collect_pic);
            like =  itemView.findViewById(R.id.video_like_pic);
            title = itemView.findViewById(R.id.video_title);
            title2 = itemView.findViewById(R.id.video_title2);
            video_text1 =  itemView.findViewById(R.id.video_text1);
            video_text2 =  itemView.findViewById(R.id.video_text2);
            video_text3 =  itemView.findViewById(R.id.video_text3);

            collect.setImageResource(isCollect?R.mipmap.collect_select:R.mipmap.collect);
            collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCollect=!isCollect;
                    Integer i = Integer.valueOf((String)video_text2.getText());
                    video_text2.setText(""+(isCollect?++i:--i));
                    collect.setImageResource(isCollect?R.mipmap.collect_select:R.mipmap.collect);
                }
            });
            like.setImageResource(isLike?R.mipmap.dianzan_select:R.mipmap.dianzan);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLike=!isLike;
                    Integer i = Integer.valueOf((String)video_text3.getText());
                    video_text3.setText(""+(isLike?++i:--i));
                    like.setImageResource(isLike?R.mipmap.dianzan_select:R.mipmap.dianzan);
                    updateCount(mDataset.get(mPosition).getVid(),2,isLike);
                }
            });

            if(itemChildClickListener!=null){
                mPlayerContainer.setOnClickListener(this);
            }
            if(itemClickListener!=null){
                itemView.setOnClickListener(this);
            }

            itemView.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.player_container){
                if(itemChildClickListener!=null){
                    itemChildClickListener.onItemChildClick(mPosition);
                }
            }else{
                if(itemClickListener!=null){
                    itemClickListener.onItemClick(mPosition);
                }
            }
        }
    }
    void updateCount(int vid,int type,boolean flag){
        Map<String,Object> map=new HashMap<>();
        map.put("vid",vid);
        map.put("type",type);
        map.put("flag",flag);
        Map<String,Object> headermap=new HashMap<>();
        SharedPreferences slq = getMContext().getSharedPreferences("slq", Context.MODE_PRIVATE);
        headermap.put("token", slq.getString("token", ""));

        Api.postRequest(ApiConfig.VIDEO_UPDATE_COUNT,headermap, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT);
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("", "onResponse: "+string );
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder h=(MyViewHolder)holder;
        HomeVideoEntity videoEntity = mDataset.get(position);
        h.title.setText(videoEntity.getVtitle());
        h.title2.setText(videoEntity.getAuthor());

        Picasso.with(mContext).load(videoEntity.getHeadurl())
                .transform(new CirclePictureTransform(mContext)).into(h.userhead);
        Picasso.with(mContext).load(videoEntity.getCoverurl()).into(h.mThumb);

        Integer text1 = videoEntity.getCommentNum();
        h.video_text1.setText(""+text1);
        Integer text2 = videoEntity.getCollectNum();
        h.video_text2.setText(""+text2);
        Integer text3 = videoEntity.getLikeNum();
        h.video_text3.setText(""+text3);
        h.mPosition=position;

    }


    @Override
    public int getItemCount() {
        return mDataset==null?0:mDataset.size();
    }


}
