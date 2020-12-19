package com.slq.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.component.PrepareView;
import com.slq.myapp.R;
import com.slq.myapp.entity.HomeVideoEntity;
import com.slq.myapp.entity.NewsEntity;
import com.slq.myapp.listener.OnObjectItemClickListener;
import com.slq.myapp.view.CirclePictureTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class NewsAdapter extends RecyclerView.Adapter {
    private List<NewsEntity> data = new ArrayList<NewsEntity>();
    Context mContext;
    @Setter
    OnObjectItemClickListener onClickListener1;
    @Setter
    OnObjectItemClickListener onClickListener2;
    public NewsAdapter(List<NewsEntity> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case 1:
                v = LayoutInflater.from(mContext).inflate(R.layout.news_item_1, parent, false);
                break;
            case 2:
                v = LayoutInflater.from(mContext).inflate(R.layout.news_item_2, parent, false);
                break;
            case 3:
                v = LayoutInflater.from(mContext).inflate(R.layout.news_item_3, parent, false);
                break;
        }

        //View v = parent.findViewById(R.id.news_item1);
        RecyclerView.ViewHolder ViewHolder = new NewsHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsHolder h = (NewsHolder) holder;
        NewsEntity entity = data.get(position);
        h.title.setText(entity.getNewsTitle());
        h.author.setText(entity.getAuthorName());
        h.comment.setText("" + entity.getCommentCount());
        h.time.setText(entity.getReleaseDate());

        Picasso.with(mContext).load(entity.getHeaderUrl()).into(h.header);
        int type = data.get(position).getType();
        h.entity=entity;
        if (type == 2) {
            Picasso.with(mContext).load(entity.getThumbEntities().get(0).getThumbUrl()).into(h.pic1);
            Picasso.with(mContext).load(entity.getThumbEntities().get(1).getThumbUrl()).into(h.pic2);
            Picasso.with(mContext).load(entity.getThumbEntities().get(2).getThumbUrl()).into(h.pic3);
            h.onClickListener=onClickListener2;
        } else {
            Picasso.with(mContext).load(entity.getThumbEntities().get(0).getThumbUrl()).into(h.thumb);
            h.onClickListener=onClickListener1;
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder{
        public ImageView thumb, header, pic1, pic2, pic3;
        public TextView title, author, comment, time;
        Object entity;
        OnObjectItemClickListener onClickListener;


        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            header = itemView.findViewById(R.id.header);
            pic1 = itemView.findViewById(R.id.pic1);
            pic2 = itemView.findViewById(R.id.pic2);
            pic3 = itemView.findViewById(R.id.pic3);

            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            comment = itemView.findViewById(R.id.comment);
            time = itemView.findViewById(R.id.time);

            itemView.setTag(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(entity);
                }
            });
        }
    }

}
