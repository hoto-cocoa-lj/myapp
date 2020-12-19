package com.slq.myapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
public class HomeVideoEntity {
    private int vid;
    private String vtitle;
    private String author;
    private String coverurl;
    private String headurl;
    private int commentNum;
    private int likeNum;
    private int collectNum;
    private String playurl;
    private String createTime;
    private String updateTime;
    private int categoryId;
    private String categoryName;
    private Object videoSocialEntity;
    @Setter
    boolean isLike,isCollect;

    public HomeVideoEntity(String vtitle, String author, String coverurl, String headurl, int commentNum, int likeNum, int collectNum) {
        this.vtitle = vtitle;
        this.author = author;
        this.coverurl = coverurl;
        this.headurl = headurl;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.collectNum = collectNum;
    }
}
