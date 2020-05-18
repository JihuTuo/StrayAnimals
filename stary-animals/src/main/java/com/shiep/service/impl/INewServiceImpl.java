package com.shiep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiep.entity.News;
import com.shiep.entity.NewsType;
import com.shiep.entity.User;
import com.shiep.mapper.INewsMapper;
import com.shiep.mapper.INewsTypeMapper;
import com.shiep.mapper.IUserMapper;
import com.shiep.service.INewsService;
import com.shiep.vo.NewsVo;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class INewServiceImpl implements INewsService {

    @Resource
    private INewsMapper newsMapper;

    @Resource
    private INewsTypeMapper newsTypeMapper;

    @Resource
    private IUserMapper userMapper;

    @Override
    public List<NewsType> queryAllNewsType() {
       return Optional.ofNullable(newsTypeMapper.selectList(null)).orElseGet(ArrayList::new);
    }


    public List<NewsVo> getNewsVos(List<News> newsList) {
        List<NewsVo> vos = new ArrayList<>();
        Optional.ofNullable(newsList).orElseGet(ArrayList::new).forEach( news -> {
            NewsVo vo = new NewsVo();
            vo.setNews(news);

            QueryWrapper<NewsType> qw = new QueryWrapper<>();
            qw.lambda().eq(NewsType::getId, news.getTypeId());
            vo.setNewsType(this.newsTypeMapper.selectOne(qw));

            QueryWrapper<User> qwUser = new QueryWrapper<>();
            qwUser.lambda().eq(User::getId, news.getUserId());
            vo.setUser(this.userMapper.selectOne(qwUser));

            vos.add(vo);
        });

        return Optional.ofNullable(vos).orElseGet(ArrayList::new);
    }

    @Override
    public List<NewsVo> queryAllNewsVos(Integer typeId) {
        List<News> newsList = null;
        if (typeId == null) {
            QueryWrapper<News> qw = new QueryWrapper<>();
            qw.lambda().eq(News::getDeleteStatus, 0);
            newsList = this.newsMapper.selectList(qw);
            return this.getNewsVos(newsList);
        }
        QueryWrapper<News> qw = new QueryWrapper<>();
        qw.lambda().eq(News::getDeleteStatus, 0).eq(News::getTypeId, typeId);
        newsList = this.newsMapper.selectList(qw);
        return this.getNewsVos(newsList);
    }

    @Override
    public Boolean saveNews(News news) {
        if (news == null) {
            return false;
        }
        // 进行截取
        String str = news.getContent();
        // 获取src中的内容，保存图片路径
        Pattern pattern = Pattern.compile("http://www.sa.com/upload/ueditor/.{40}");
        Pattern pattern2 = Pattern.compile("http://www.sa.com" +"\\\\upload\\\\ueditor\\\\.{40}");
        Matcher matcher = pattern.matcher(str);
        Matcher matche2 = pattern.matcher(str);
        if (matcher.find()) {
           news.setPhoto(matcher.group());
        } else if (matche2.find()){
            news.setPhoto(matcher.group());
        } else {
            // 匹配不到，设置默认图片
            news.setPhoto("http://www.sa.com/upload/ueditor/f6707879-2f8f-427e-b37c-d800e5ce8be4.jpg");
        }


        return this.newsMapper.insert(news) > 0;
    }


    @Override
    public List<NewsVo> queryAllNewsVos() {
        List<News> newsList = null;
        newsList = this.newsMapper.selectList(null);
        return this.getNewsVos(newsList);
    }

    @Override
    public Boolean deleteNewsById(Long id) {
        if (id == null) {
            return false;
        }
        return this.newsMapper.deleteNewsById(id) > 0;
    }

    @Override
    public Boolean rollbackNewsById(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        ids.forEach(id -> {
            this.newsMapper.rollbackNewsById(id);
        });
        return true;
    }

    @Override
    public Boolean deleteNewsByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
       ids.forEach( id -> {
           this.newsMapper.deleteNewsById(id);
        });
        return true;
    }

    @Override
    public News getNewsById(Long id) {
        if (id == null) {
            return null;
        }
        News news = this.newsMapper.selectById(id);
        return news;
    }

    @Override
    public Boolean updateNews(News editNews) {
        if (editNews.getId() == null) {
            return false;
        }

        UpdateWrapper<News> uw = new UpdateWrapper<>();
        uw.lambda().eq(News::getId, editNews.getId());

        return this.newsMapper.update(editNews, uw) > 0;
    }

    @Override
    public NewsVo getNewsDetailsById(Long id) {
        if (id == null) {
            return null;
        }
        NewsVo vo = new NewsVo();
        QueryWrapper<News> qw = new QueryWrapper<>();
        qw.lambda().eq(News::getDeleteStatus, 0).eq(News::getId, id);
        News news = this.newsMapper.selectOne(qw);
        if (news == null) {
            return null;
        }

        vo.setNews(news);


        QueryWrapper<NewsType> qwType = new QueryWrapper<>();
        qwType.lambda().eq(NewsType::getId, news.getTypeId());
        vo.setNewsType(this.newsTypeMapper.selectOne(qwType));

        QueryWrapper<User> qwUser = new QueryWrapper<>();
        qwUser.lambda().eq(User::getId, news.getUserId());
        vo.setUser(this.userMapper.selectOne(qwUser));

        return vo;
    }
}
