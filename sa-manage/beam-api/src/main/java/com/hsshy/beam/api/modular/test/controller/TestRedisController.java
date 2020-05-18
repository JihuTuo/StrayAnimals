package com.hsshy.beam.api.modular.test.controller;

import com.hsshy.beam.api.modular.test.dto.LoginForm;
import com.hsshy.beam.common.annotion.IgnoreUTokenAuth;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.redis.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 测试redis
 * @author: hs
 * @create: 2019-08-04 14:09:52
 **/
//@Api(value = "测试redis",tags = "TestRedisController")
@RequestMapping(value = "/redis")
@RestController
public class TestRedisController {

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "放入缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/set")
    public R set(){

        Map map = new HashMap();
        map.put("test1","v1");
        map.put("test2","v2");
        map.put("test3","v3");
        redisUtil.set("value:test",map);
        return R.ok();
    }

    @ApiOperation(value = "取出缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/get")
    public R  get(){

        return R.ok(redisUtil.get("test"));
    }

    @ApiOperation(value = "删除缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/del")
    public R  del(String key){
        redisUtil.deleteKey(key);
        return R.ok();
    }


    @ApiOperation(value = "放入缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/list/push")
    public R lpush(){

        Map map = new HashMap();
        map.put("test1","v1");
        map.put("test2","v2");
        map.put("test3","v3");
        redisUtil.lpush("list:test",map);
        return R.ok();
    }

    @ApiOperation(value = "取出缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/list/pop")
    public R  lpop(){

        return R.ok((Map)redisUtil.lpop("list:test"));
    }

    @ApiOperation(value = "读取缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/list/get")
    public R  lget(){

        return R.ok(redisUtil.lget("list:test",0,-1));
    }



    @ApiOperation(value = "读取缓存根据index")
    @IgnoreUTokenAuth
    @GetMapping(value = "/list/get/index")
    public R  lgetIndex(int index){

        return R.ok((Map)redisUtil.lgetIndex("list:test",index));
    }

    @ApiOperation(value = "更新缓存根据index")
    @IgnoreUTokenAuth
    @GetMapping(value = "/list/update/index")
    public R  lupdateIndex(int index){
        Map map = new HashMap();
        map.put("test1","v1");
        map.put("test3","v3");
        return R.ok(redisUtil.lupdateIndex("list:test",index,map));
    }

    @ApiOperation(value = "移除缓存根据index")
    @IgnoreUTokenAuth
    @GetMapping(value = "/list/remove/index")
    public R  lremoveIndex(int index){
        Map map = new HashMap();
        map.put("test1","v1");
        map.put("test2","v2");
        map.put("test3","v3");
        return R.ok(redisUtil.lremove("list:test",index,map));
    }

    @ApiOperation(value = "放入缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/set/set")
    public R  sSet(){

        for(int i=0;i<10;i++){
            LoginForm loginForm = new LoginForm();
            loginForm.setUsername("test"+i);
            loginForm.setPassword("password"+i);
            redisUtil.sSet("set:test",loginForm);
        }


        return R.ok();
    }

    @ApiOperation(value = "读取缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/set/members")
    public R  sMembers(){

        Set<LoginForm> mapSet = redisUtil.sMembers("set:test");
        return R.ok(mapSet);
    }

    @ApiOperation(value = "读取缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/set/scan")
    public R  sScan(){

        List<LoginForm> mapSet = redisUtil.sScan("set:test");
        return R.ok(mapSet);
    }

    @ApiOperation(value = "移除缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/set/remove")
    public R  sRemove(){

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("test"+5);
        loginForm.setPassword("password"+5);

        Long count = redisUtil.sRemove("set:test",loginForm);
        return R.ok(count);
    }

    @ApiOperation(value = "放入缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/zset/set")
    public R  zSet(){

        for(int i=0;i<10;i++){
            LoginForm loginForm = new LoginForm();
            loginForm.setUsername("test"+i);
            loginForm.setPassword("password"+i);
            redisUtil.zSet("zset:test",loginForm,i);
        }

        return R.ok();
    }

    @ApiOperation(value = "读取缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/zset/range")
    public R  zRange(){

        Set<LoginForm> mapSet = redisUtil.zRange("zset:test");
        return R.ok(mapSet);
    }

    @ApiOperation(value = "读取缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/zset/scan")
    public R  zScan(){

        List<LoginForm> mapSet = redisUtil.zScan("zset:test");
        return R.ok(mapSet);
    }

    @ApiOperation(value = "移除缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/zset/remove")
    public R  zRemove(){

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("test"+2);
        loginForm.setPassword("password"+2);

        Long count = redisUtil.zRemove("set:test",loginForm);
        return R.ok(count);
    }

    @ApiOperation(value = "放入缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/hash/set")
    public R  hSet(){


        redisUtil.hset("hash:test","hkey","asadsda");

        return R.ok();
    }

    @ApiOperation(value = "放入缓存")
    @IgnoreUTokenAuth
    @GetMapping(value = "/hash/set/all")
    public R  hSetAll(){

        Map map = new HashMap();
        map.put("hkey","hhhhhh");
        map.put("test1","111111");
        map.put("test2","222222");

        redisUtil.hsetAll("hash:test",map);

        return R.ok();
    }

    @ApiOperation(value = "获取map")
    @IgnoreUTokenAuth
    @GetMapping(value = "/hash/get/map")
    public R  hGetMap(){

        return R.ok(redisUtil.hgetMap("hash:test"));
    }

    @ApiOperation(value = "获取值")
    @IgnoreUTokenAuth
    @GetMapping(value = "/hash/get")
    public R  hget(){

        return R.ok(redisUtil.hget("hash:test","hkey"));
    }

    @ApiOperation(value = "获取的所有value")
    @IgnoreUTokenAuth
    @GetMapping(value = "/hash/get/value/list")
    public R  hgetVlist(){

        return R.ok(redisUtil.hgetVlist("hash:test"));
    }

    @ApiOperation(value = "扫描key的所有值")
    @IgnoreUTokenAuth
    @GetMapping(value = "/hash/scan")
    public R  hscan(){

        return R.ok(redisUtil.hscan("hash:test"));
    }

}
