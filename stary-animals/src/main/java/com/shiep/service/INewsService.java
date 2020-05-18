package com.shiep.service;

import com.shiep.entity.News;
import com.shiep.entity.NewsType;
import com.shiep.vo.NewsVo;
import io.swagger.models.auth.In;

import javax.persistence.Transient;
import java.util.List;

public interface INewsService {

    List<NewsType> queryAllNewsType();

    List<NewsVo> queryAllNewsVos(Integer typeId);

    List<NewsVo> queryAllNewsVos();

    @Transient
    Boolean saveNews(News news);

    @Transient
    Boolean deleteNewsById(Long id);

    @Transient
    Boolean rollbackNewsById(List<Long> ids);

    @Transient
    Boolean deleteNewsByIds(List<Long> ids);

    News getNewsById(Long id);

    Boolean updateNews(News editNews);

    NewsVo getNewsDetailsById(Long id);
}
