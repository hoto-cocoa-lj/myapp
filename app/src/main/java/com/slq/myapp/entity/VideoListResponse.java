package com.slq.myapp.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VideoListResponse implements Serializable {

    private String msg;
    private int code;
    private PageBean page;

    @NoArgsConstructor
    @Data
    public static class PageBean {
        private int totalCount;
        private int pageSize;
        private int totalPage;
        private int currPage;
        private List<HomeVideoEntity> list;
    }
}
