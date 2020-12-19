package com.slq.myapp.api;
//public static final String BASE_URL="http://192.168.31.166:8080/renren-fast";
public class ApiConfig {
    public static final String BASE_URL="http://192.168.31.166:8080/renren-fast";

    public static final String LOGIN="/app/login";
    public static final String REGISTER="/app/register";
    public static final String HOME_VIDEO_LIST="/app/videolist/list";
    public static final String HOME_VIDEO_LIST_BY_CATEGORY="/app/videolist/getListByCategoryId";
    public static final String HOME_VIDEO_CATEGORY_LIST = "/app/videocategory/list";//视频类型列表
    public static final String NEWS_LIST = "/app/news/api/list";//资讯列表
    public static final String VIDEO_UPDATE_COUNT = "/app/videolist/updateCount";//更新点赞,收藏,评论
    public static final String VIDEO_MYCOLLECT = "/app/videolist/mycollect";//我的收藏

    public static final int PAGE_SIZE=2;
}
