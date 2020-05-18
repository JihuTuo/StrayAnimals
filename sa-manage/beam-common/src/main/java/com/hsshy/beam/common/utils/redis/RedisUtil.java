package com.hsshy.beam.common.utils.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Redis工具类
 *
 * @author hs
 */
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Object> hashOperations;
    @Resource(name = "redisTemplate")
    private ListOperations<String, Object> listOperations;
    @Resource(name = "redisTemplate")
    private SetOperations<String, Object> setOperations;
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, Object> zSetOperations;
    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;


    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNotExist(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public void deleteKey(String... keys) {
        Set<String> kSet = Stream.of(keys).map(k -> k).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 删除Key的集合
     *
     * @param keys
     */
    public void deleteKey(Collection<String> keys) {
        Set<String> kSet = keys.stream().map(k -> k).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     *
     * @param key
     * @param date
     */
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     *
     * @param key
     * @param timeUnit
     * @return
     */
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key
     */
    public void persistKey(String key) {
        redisTemplate.persist(key);
    }


    /**
     * valueOperations 使用
     */
    public void set(String key, Object value, long expire) {
        valueOperations.set(key, toJson(value));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {

        return get(key, NOT_EXPIRE);
    }

    /**
     * @method
     * @description 加
     * @date: 2019/8/5 17:48
     * @param  * @param null
     * @return
     */
    public long increment(String key,long count,long expire){
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return valueOperations.increment(key,count);
    }
    public long increment(String key,long count){

        return increment(key,count,DEFAULT_EXPIRE);
    }



    /**
     * listOperations使用
     */
    public <T> void lpush(String key, T t, long expire) {
        listOperations.leftPush(key, t);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * @method
     * @description 放入缓存
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> void lpush(String key, T t) {
        lpush(key, t, DEFAULT_EXPIRE);
    }

    public <T> T lpop(String key, long expire) {
        T t = (T) listOperations.leftPop(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return t;
    }


    /**
     * @method
     * @description 从list中获取最左边的值
     * @date: 2019/8/2 18:13
     * @param  * @param key
     * @return
     */
    public <T> T lpop(String key) {
        return lpop(key, NOT_EXPIRE);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public <T> List<T> lget(String key, long start, long end) {
        List<T> list = (List<T>) listOperations.range(key, start, end);
        return list;
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public <T> T lgetIndex(String key, long index) {
        try {
            return (T) listOperations.index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lupdateIndex(String key, long index, Object value) {
        try {
            listOperations.set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lremove(String key, long count, Object value) {
        try {
            Long remove = listOperations.remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * setOperations使用
     */

    public <T> void sSet(String key, T t, long expire) {
        setOperations.add(key, t);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * @method
     * @description 放入缓存
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> void sSet(String key, T t) {
        sSet(key, t, DEFAULT_EXPIRE);
    }
    /**
     * @method
     * @description 获取
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> Set<T> sMembers(String key){
        return (Set<T>)setOperations.members(key);
    }

    /**
     * @method
     * @description 扫描
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> List<T> sScan(String key){
        List<T> arrayList = new ArrayList<>();
        try {
            Cursor<Object> cursor = setOperations.scan(key,ScanOptions.scanOptions().match("*").count(1000).build());
            while (cursor.hasNext()) {
                Object valueSet = cursor.next();
                arrayList.add((T)valueSet);
            }
            //关闭scan
            cursor.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    /**
     * @method
     * @description 移除
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> Long sRemove(String key,T... t){
        return setOperations.remove(key,t);
    }


    /**
     * zSetOperations使用
     */

    public <T> void zSet(String key, T t,double score, long expire) {
        zSetOperations.add(key, t,score);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * @method
     * @description 放入缓存
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> void zSet(String key, T t,double score) {
        zSet(key, t,score, DEFAULT_EXPIRE);
    }
    /**
     * @method
     * @description 获取缓存数据
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> Set<T> zRange(String key){
        return (Set<T>)zSetOperations.range(key,0,-1);
    }



    /**
     * @method
     * @description 获取元素排名
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> Long zRank(String key,T t){
        return zSetOperations.rank(key,t);
    }

    /**
     * @method
     * @description 移除
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> Long zRemove(String key,T... t){
        return zSetOperations.remove(key,t);
    }

    /**
     * @method
     * @description 扫描
     * @date: 2019/8/4 13:59
     * @param  * @param null
     * @return
     */
    public <T> List<T> zScan(String key){
        List<T> arrayList = new ArrayList<>();
        try {
            Cursor<ZSetOperations.TypedTuple<Object>> cursor = zSetOperations.scan(key,
                    // count=1000 为每次扫描的条数
                    ScanOptions.scanOptions().match("*").count(1000).build());
            while (cursor.hasNext()) {
                Object valueSet = cursor.next().getValue();
                arrayList.add((T)valueSet);
            }
            //关闭scan
            cursor.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * hashOperations使用
     */


    public void hsetAll(String key,Map map, long expire){
        hashOperations.putAll(key, map);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }
    /**
     * @method
     * @description 放入Map
     * @date: 2019/8/4 13:59
     * @return
     */
    public  void hsetAll(String key,Map map){
        hsetAll(key,map,DEFAULT_EXPIRE);
    }

    public  void hset(String key,String hkey,String hvalue,long expire){
        hashOperations.put(key, hkey,hvalue);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }
    /**
     * @method
     * @description 放入String
     * @date: 2019/8/4 13:59
     * @return
     */
    public  void hset(String key,String hkey,String hvalue){
        hset(key,hkey,hvalue,DEFAULT_EXPIRE);
    }
    /**
     * @method
     * @description 获取map
     * @date: 2019/8/4 13:59
     * @return
     */
    public Map hgetMap(String key){

        return hashOperations.entries(key);

    }
    /**
     * @method
     * @description 获取单个值
     * @date: 2019/8/4 13:59
     * @return
     */
    public <T> T hget(String key,String hkey){

        return (T)hashOperations.get(key,hkey);

    }
    /**
     * @method
     * @description 获取所有值
     * @date: 2019/8/4 13:59
     * @return
     */
    public <T> List<T> hgetVlist(String key){

        return (List<T>)hashOperations.values(key);

    }
    /**
     * @method
     * @description 扫描获取对象
     * @date: 2019/8/4 13:59
     * @return
     */
    public List<Map.Entry<String, Object>> hscan(String key){

        List<Map.Entry<String, Object>> mapList = new ArrayList<>();
        Cursor<Map.Entry<String, Object>> curosr = hashOperations.scan(key,
                ScanOptions.scanOptions().match("*").count(1000).build());
        while(curosr.hasNext()){
            Map.Entry<String, Object> entry = curosr.next();
            mapList.add(entry);
        }

        return mapList;

    }



    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }


    @CacheEvict(value = {"CONSTANT"}, allEntries = true)
    public void clearCache() {

    }
}
