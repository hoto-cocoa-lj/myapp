package com.slq.myapp.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NewsEntity implements Serializable {
        private int newsId;
        private String newsTitle;
        private String authorName;
        private String headerUrl;
        private int commentCount;
        private String releaseDate;
        private int type;
        private Object imgList;
        private List<ThumbEntitiesBean> thumbEntities;

        @NoArgsConstructor
        @Data
        public static class ThumbEntitiesBean {
            public int thumbId;
            public String thumbUrl;
            public int newsId;
        }

}
