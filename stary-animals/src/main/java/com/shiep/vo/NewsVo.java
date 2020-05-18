package com.shiep.vo;

import com.shiep.entity.News;
import com.shiep.entity.NewsType;
import com.shiep.entity.User;
import lombok.Data;

@Data
public class NewsVo {
    private News news;
    private NewsType newsType;
    private User user;
}
