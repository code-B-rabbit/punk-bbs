package com.example.xhbblog.manager;


import com.example.xhbblog.mapper.UserMapper;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.utils.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
@CacheConfig(cacheNames = "user")
public class RedisUserManager {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Cacheable(key = "#id")
    public User get(Integer id) {
        logger.info("用户{}缓存未命中",id);
        return userMapper.selectByPrimaryKey(id);
    }

    @Cacheable(key = "#name")
    public User uid(String name) {
        return userMapper.getUid(name);
    }

    public Long msgCnt(Integer uid) {
        Long size= redisTemplate.opsForList().size(RedisKey.NOT_READ_COMMENT_LIST+uid);
        return size==null?0:size;
    }

    public List<String> getMessages(Integer uid) {
        String key= RedisKey.NOT_READ_COMMENT_LIST+uid;
        List<String> messages=new ArrayList<>();
        while(redisTemplate.hasKey(key)){       //有未读信息则全部推送过去
            String message= (String) redisTemplate.opsForList().leftPop(key);
            messages.add(message);
        }
        return messages;
    }

    public void sendMessageTo(Integer uid,String message){
        redisTemplate.opsForList().leftPush(RedisKey.NOT_READ_COMMENT_LIST+uid,message);  //放入redis list对象里去
    }

    //消息关闭时为设置已读的消息
    public void addMessages(Integer uid,String []messages){
        redisTemplate.opsForList().rightPushAll(RedisKey.NOT_READ_COMMENT_LIST+uid,messages);
    }

    public void delUsr(Integer uid){
        redisTemplate.delete(RedisKey.USR+uid);
    }

    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
        redisTemplate.opsForValue().set(RedisKey.USR+user.getId(),user);
    }



}
