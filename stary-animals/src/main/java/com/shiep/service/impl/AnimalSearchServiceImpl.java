package com.shiep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiep.dao.IUserDao;
import com.shiep.entity.*;
import com.shiep.mapper.*;
import com.shiep.service.IAnimalSearchService;
import com.shiep.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnimalSearchServiceImpl implements IAnimalSearchService {
    @Resource
    private IAnimalSearchMapper animalSearchMapper;

    @Resource
    private IUserDao userDao;

    @Resource
    private ILocationDao locationDao;

    @Resource
    private IAnimalCategoryDetailsMapper animalCategoryDetailsMapper;

    @Resource
    private IPhotoAnimalSearchMapper photoAnimalSearchMapper;

    @Resource
    private ICountyMapper countyMapper;

    @Resource
    private IAnimalSearchCommentMapper animalSearchCommentMapper;

    @Resource
    private IUserMapper userMapper;


    @Override
    public boolean saveAnimalSearch(AnimalSearch animalSearch) {
        if (animalSearch == null) {
            return false;
        }
        return this.animalSearchMapper.insert(animalSearch) > 0;
    }


    @Override
    public List<AnimalSearchVo> queryAll(
            Long cityId, Integer categoryId, Long countyId, Integer searchAnimal) {
        List<AnimalSearch> animalSearches = null;

        // 1、）什么都不做  首先必须处理的是cityId 如果为null  查询所有adopt
        if (cityId == null) {
            QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
            // 没有被删除 并且审核通过的
            qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1);
            animalSearches = this.animalSearchMapper.selectList(qw);

            List<AnimalSearchVo> animalSearchVos = this.getAnimalSearchVo(animalSearches);
            return animalSearchVos;
        }
        // 2、) 选择地区后：其他都为null
        if (cityId != null && (categoryId == null || categoryId == 0) && countyId == null && searchAnimal == null) {
            // 首先根据cityId查询出所有的县区id
            QueryWrapper<County> qwCounty = new QueryWrapper<>();
            qwCounty.lambda().eq(County::getCityId, cityId);
            List<County> counties = this.countyMapper.selectList(qwCounty);
            List<Long> countyIds = counties.stream().map(County::getId).collect(Collectors.toList());
            if (countyIds.isEmpty()) {
                return new ArrayList<>();
            }
            // 查找
            QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
            qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1)
                    .in(AnimalSearch::getCountyId, countyIds);
            animalSearches = this.animalSearchMapper.selectList(qw);

            List<AnimalSearchVo> AnimalSearchVos = this.getAnimalSearchVo(animalSearches);
            return AnimalSearchVos;
        }
        // 3、） 选择了具体的县区
        if (cityId != null && (categoryId == null || categoryId == 0) && countyId != null && searchAnimal == null) {
            QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
            // 没有被删除 并且审核通过的  h有具体县区
            qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1)
                        .eq(AnimalSearch::getCountyId, countyId);
            animalSearches = this.animalSearchMapper.selectList(qw);

            List<AnimalSearchVo> animalSearchVos = this.getAnimalSearchVo(animalSearches);
            return animalSearchVos;
        }
        // 4、）选择了宠类
        if (cityId != null && categoryId != null && countyId == null && searchAnimal == null) {
            // 首先根据cityId查询出所有的县区id
            QueryWrapper<County> qwCounty = new QueryWrapper<>();
            qwCounty.lambda().eq(County::getCityId, cityId);
            List<County> counties = this.countyMapper.selectList(qwCounty);
            List<Long> countyIds = counties.stream().map(County::getId).collect(Collectors.toList());
            if (countyIds.isEmpty()) {
                return new ArrayList<>();
            }

            // 其次根据宠类ID选择出具体的宠类id
            QueryWrapper<AnimalCategoryDetails> qwCategory = new QueryWrapper<>();
            qwCategory.lambda().eq(AnimalCategoryDetails::getCategoryId, categoryId);
            List<AnimalCategoryDetails> details = this.animalCategoryDetailsMapper.selectList(qwCategory);
            List<Integer> detailsIds = details.stream().map(AnimalCategoryDetails::getId).collect(Collectors.toList());
            // 查找
                // 记住，mybatisPlus 在查询来的集合为null的前提下 不会组装in的SQL，所以，所有的in我们要自己判断一下
            if (detailsIds.isEmpty()) {
                return new ArrayList<>();
            }

            QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
            qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1)
                    .in(AnimalSearch::getCountyId, countyIds).in(AnimalSearch::getCategoryId, detailsIds);
            animalSearches = this.animalSearchMapper.selectList(qw);

            List<AnimalSearchVo> animalSearchVos = this.getAnimalSearchVo(animalSearches);
            return animalSearchVos;
        }
        // 5、）选择了搜索类型 --宠物、主人
        if (cityId != null && (categoryId == null || categoryId == 0) && countyId == null && searchAnimal != null) {
            // 首先根据cityId查询出所有的县区id
            QueryWrapper<County> qwCounty = new QueryWrapper<>();
            qwCounty.lambda().eq(County::getCityId, cityId);
            List<County> counties = this.countyMapper.selectList(qwCounty);
            List<Long> countyIds = counties.stream().map(County::getId).collect(Collectors.toList());
            if (countyIds.isEmpty()) {
                return new ArrayList<>();
            }
            // 查找
            QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
            qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1)
                    .in(AnimalSearch::getCountyId, countyIds).eq(AnimalSearch::getSearchAnimal, searchAnimal);
            animalSearches = this.animalSearchMapper.selectList(qw);

            List<AnimalSearchVo> animalSearchVos = this.getAnimalSearchVo(animalSearches);
            return animalSearchVos;
        }
        // 6、）选了具体的县区+宠类
        if (cityId != null && categoryId != null && countyId != null && searchAnimal == null) {
            // 先根据宠类ID选择出具体的宠类id
            QueryWrapper<AnimalCategoryDetails> qwCategory = new QueryWrapper<>();
            qwCategory.lambda().eq(AnimalCategoryDetails::getCategoryId, categoryId);
            List<AnimalCategoryDetails> details = this.animalCategoryDetailsMapper.selectList(qwCategory);
            List<Integer> detailsIds = details.stream().map(AnimalCategoryDetails::getId).collect(Collectors.toList());
            if (detailsIds.isEmpty()) {
                return new ArrayList<>();
            }
            QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
            // 没有被删除 并且审核通过的  h有具体县区 + 宠类
            qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1)
                    .eq(AnimalSearch::getCountyId, countyId).in(AnimalSearch::getCategoryId, detailsIds);
            animalSearches = this.animalSearchMapper.selectList(qw);

            List<AnimalSearchVo> animalSearchVos = this.getAnimalSearchVo(animalSearches);
            return animalSearchVos;
        }
        // 7、）选了具体的县区和寻找类型
        if (cityId != null && (categoryId == null || categoryId == 0) && countyId != null && searchAnimal != null) {
            // 查找
            QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
            // 没有被删除 并且审核通过的  h有具体县区
            qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1)
                    .eq(AnimalSearch::getCountyId, countyId).eq(AnimalSearch::getSearchAnimal, searchAnimal);
            animalSearches = this.animalSearchMapper.selectList(qw);

            List<AnimalSearchVo> animalSearchVos = this.getAnimalSearchVo(animalSearches);
            return animalSearchVos;
        }
        // 8、）选择了宠类和寻找分类
        if (cityId != null && categoryId != null && countyId == null && searchAnimal != null) {
            // 首先根据cityId查询出所有的县区id
            QueryWrapper<County> qwCounty = new QueryWrapper<>();
            qwCounty.lambda().eq(County::getCityId, cityId);
            List<County> counties = this.countyMapper.selectList(qwCounty);
            List<Long> countyIds = counties.stream().map(County::getId).collect(Collectors.toList());
            if (countyIds.isEmpty()) {
                return new ArrayList<>();
            }
            // 其次根据宠类ID选择出具体的宠类id
            QueryWrapper<AnimalCategoryDetails> qwCategory = new QueryWrapper<>();
            qwCategory.lambda().eq(AnimalCategoryDetails::getCategoryId, categoryId);
            List<AnimalCategoryDetails> details = this.animalCategoryDetailsMapper.selectList(qwCategory);
            List<Integer> detailsIds = details.stream().map(AnimalCategoryDetails::getId).collect(Collectors.toList());
            if (detailsIds.isEmpty()) {
                return new ArrayList<>();
            }
            // 查找
            QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
            // 没有被删除 并且审核通过的  县区ids  分类ids
            qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1)
                    .eq(AnimalSearch::getSearchAnimal, searchAnimal)
                    .in(AnimalSearch::getCategoryId, detailsIds).in(AnimalSearch::getCountyId, countyIds);
            animalSearches = this.animalSearchMapper.selectList(qw);

            List<AnimalSearchVo> animalSearchVos = this.getAnimalSearchVo(animalSearches);
            return animalSearchVos;
        }
        // 9、）选了宠类和寻找类型
        return null;
    }

    @Override
    public List<AnimalSearch> queryAllByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
        qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1).eq(AnimalSearch::getUserId, userId);
        List<AnimalSearch> animalSearches = this.animalSearchMapper.selectList(qw);
        if (CollectionUtils.isEmpty(animalSearches)) {
            return null;
        }
        return animalSearches;
    }

    @Override
    public boolean removeAnimalSearchById(Long id) {
        if (id == null) {
            return false;
        }
        if (this.animalSearchMapper.removeAnimalSearchById(id) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public AnimalSearchVo getAnimalSearchDetailsById(Long id) {
        if (id == null) {
            return null;
        }
        QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
        qw.lambda().eq(AnimalSearch::getDeleteStatus, 0).eq(AnimalSearch::getVerifyStatus, 1).eq(AnimalSearch::getId, id);
        AnimalSearch a = this.animalSearchMapper.selectOne(qw);

        if (a == null) {
            return null;
        }

        AnimalSearchVo vo = new AnimalSearchVo();

        User user = this.userDao.getUserById(a.getUserId());
        Location location = this.locationDao.getLocationByCountyId(a.getCountyId());
        Category category = this.animalCategoryDetailsMapper.getCategoryById(a.getCategoryId());

        List<PhotoAnimalSearch> photos = this.photoAnimalSearchMapper.getPhotoByAnimalSearchId(a.getId());

        vo.setAnimalSearch(a);
        vo.setUser(user);
        vo.setLocation(location);
        vo.setCategory(category);
        vo.setPhotoAnimalSearch(photos);
        return vo;
    }


    /**
     * 处理 AnimalSearchs
     *
     * @param AnimalSearches
     * @return
     */
    public List<AnimalSearchVo> getAnimalSearchVo (List<AnimalSearch> AnimalSearches) {
        List<AnimalSearchVo> searchVos = new ArrayList<>();
        if (CollectionUtils.isEmpty(AnimalSearches)) {
            return null;
        }
        for (AnimalSearch a : AnimalSearches) {
            AnimalSearchVo vo = new AnimalSearchVo();

            User user = this.userDao.getUserById(a.getUserId());
            Location location = this.locationDao.getLocationByCountyId(a.getCountyId());
            Category category = this.animalCategoryDetailsMapper.getCategoryById(a.getCategoryId());

            List<PhotoAnimalSearch> photos = this.photoAnimalSearchMapper.getPhotoByAnimalSearchId(a.getId());

            vo.setAnimalSearch(a);
            vo.setUser(user);
            vo.setLocation(location);
            vo.setCategory(category);
            vo.setPhotoAnimalSearch(photos);
            searchVos.add(vo);
        }
        return searchVos;
    }

    @Override
    public boolean saveSearchComment(AnimalSearchComment searchComment) {
        if (searchComment == null) {
            return false;
        }
        return this.animalSearchCommentMapper.insert(searchComment) > 0;
    }

    @Override
    public List<AnimalSearchCommentVo> queryCommentBySearchId(Long searchId) {
        if (searchId == null) {
            return null;
        }
        List<AnimalSearchCommentVo> commentVos = new ArrayList<>();

        QueryWrapper<AnimalSearchComment> qw = new QueryWrapper<>();
        qw.lambda().eq(AnimalSearchComment::getDeleteStatus, 0).eq(AnimalSearchComment::getSearchId, searchId);
        List<AnimalSearchComment> list = this.animalSearchCommentMapper.selectList(qw);

        for(AnimalSearchComment comment : list) {
            AnimalSearchCommentVo vo = new AnimalSearchCommentVo();
            vo.setSearchComment(comment);

            QueryWrapper<User> qwUser = new QueryWrapper<>();
            qwUser.lambda().eq(User::getId, comment.getCommentUserId());
            User user = this.userMapper.selectOne(qwUser);

            vo.setUser(user);

            commentVos.add(vo);
        }

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return commentVos;
    }


    @Override
    public List<AnimalSearchVo> queryAllSearchInPageBack() {
        List<AnimalSearch> searches = this.animalSearchMapper.selectList(null);
        if (CollectionUtils.isEmpty(searches)) {
            return null;
        }
        List<AnimalSearchVo> animalSearchVo = this.getAnimalSearchVo(searches);
        if (CollectionUtils.isEmpty(animalSearchVo)) {
            return null;
        }
        return animalSearchVo;
    }

    @Override
    public boolean deleteMultipleSearchByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        for (Long id : ids) {
            if (id != null) {
               try {
                   this.animalSearchMapper.removeAnimalSearchById(id);
               } catch (Exception e) {
                   return false;
               }
            }
        }
        return true;
    }

    @Override
    public boolean rollbackMultipleSearchByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        for (Long id : ids) {
            if (id != null) {
                try {
                    this.animalSearchMapper.rollbackMultipleSearchById(id);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public AnimalSearch queryAnimalSearchById(Long id) {
        if (id == null) {
            return null;
        }
        return this.animalSearchMapper.selectById(id);
    }

    @Override
    public List<PhotoAnimalSearch> queryPhotoBySearchId(Long id) {
        if (id == null) {
            return null;
        }
        QueryWrapper<PhotoAnimalSearch> qw = new QueryWrapper<>();
        qw.lambda().eq(PhotoAnimalSearch::getAnimalSearchId, id);
        return this.photoAnimalSearchMapper.selectList(qw);
    }

    @Override
    public boolean updateSearchBack(AnimalSearch animalSearchInfo) {
        if (animalSearchInfo.getId() == null) {
            return false;
        }
        UpdateWrapper<AnimalSearch> qw = new UpdateWrapper<>();
        qw.lambda().eq(AnimalSearch::getId, animalSearchInfo.getId());
        return this.animalSearchMapper.update(animalSearchInfo, qw) > 0;
    }

    @Override
    public List<AnimalSearchVo> querySearchByLike(String key, String value) {
        if (StringUtils.isBlank(value)) {
            List<AnimalSearch> searches = this.animalSearchMapper.selectList(null);
            return this.getAnimalSearchVo(searches);
        }
        QueryWrapper<AnimalSearch> qw = new QueryWrapper<>();
        qw.like(key, value);
        List<AnimalSearch> searches = this.animalSearchMapper.selectList(qw);
        List<AnimalSearchVo> animalSearchVo = this.getAnimalSearchVo(searches);

        if (CollectionUtils.isEmpty(animalSearchVo)) {
            return null;
        }
        return animalSearchVo;
    }


    public List<AnimalSearchCommentVo> getAnimalSearchCommentVo(List<AnimalSearchComment> comments) {
        if (CollectionUtils.isEmpty(comments)) {
            return null;
        }
        List<AnimalSearchCommentVo> commentVos = new ArrayList<>();
        for(AnimalSearchComment comment : comments) {
            AnimalSearchCommentVo vo = new AnimalSearchCommentVo();
            vo.setSearchComment(comment);

            QueryWrapper<User> qwUser = new QueryWrapper<>();
            qwUser.lambda().eq(User::getId, comment.getCommentUserId());
            User user = this.userMapper.selectOne(qwUser);

            vo.setUser(user);

            commentVos.add(vo);
        }
        return commentVos;
    }

    @Override
    public List<AnimalSearchCommentVo> queryAllCommentInPageBack() {
        List<AnimalSearchComment> comments = this.animalSearchCommentMapper.selectList(null);
        if (CollectionUtils.isEmpty(comments)) {
            return null;
        }
        return this.getAnimalSearchCommentVo(comments);
    }

    @Override
    public boolean deleteCommentById(Long id) {
        if (id == null) {
            return false;
        }
        return this.animalSearchCommentMapper.deleteCommentById(id) > 0;
    }

    @Override
    public boolean deleteMultipleCommentsByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        for (Long id : ids) {
            if (id != null)
                try {
                    this.animalSearchCommentMapper.deleteCommentById(id);
                } catch (Exception e) {
                    System.out.println(e) ;
                    return false;
                }
        }
        return true;
    }

    @Override
    public boolean rollbackMultipleCommentsByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        for (Long id : ids) {
            if (id != null)
                try {
                    this.animalSearchCommentMapper.rollbackCommentById(id);
                } catch (Exception e) {
                    System.out.println(e) ;
                    return false;
                }
        }
        return true;
    }

    @Override
    public List<AnimalSearchCommentVo> queryAllCommentLikeInPageBack(String key, String value) {
        if (StringUtils.isBlank(value)) {
            List<AnimalSearchComment> adopts = this.animalSearchCommentMapper.selectList(null);
            return this.getAnimalSearchCommentVo(adopts);
        }


        QueryWrapper<AnimalSearchComment> qw = new QueryWrapper<>();
        qw.like(key, value);
        List<AnimalSearchComment> adopts = this.animalSearchCommentMapper.selectList(qw);
        List<AnimalSearchCommentVo> animalAdoptVo = this.getAnimalSearchCommentVo(adopts);

        if (CollectionUtils.isEmpty(animalAdoptVo)) {
            return null;
        }
        return animalAdoptVo;
    }
}













