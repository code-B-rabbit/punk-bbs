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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUserManager {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    public User get(Integer id) {
        if(redisTemplate.hasKey(RedisKey.USR+id)==false){
            logger.info("用户{}缓存未命中",id);
            redisTemplate.opsForValue().set(RedisKey.USR+id,userMapper.selectByPrimaryKey(id));
        }
        return (User) redisTemplate.opsForValue().get(RedisKey.USR+id);
    }

    public User uid(String name) {
        if(redisTemplate.hasKey(RedisKey.USR_NAME+name)==false){
            logger.info("匿名缓存未命中");
            redisTemplate.opsForValue().set(RedisKey.USR_NAME+name,userMapper.getUid(name));
        }
        return (User) redisTemplate.opsForValue().get(RedisKey.USR_NAME+name);
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

    public String setEmailCode(String email){
        String s="";
        for (int i = 0; i < 4; i++) {
            s+=(int)(Math.random()*10);     //生成验证码
        }
        redisTemplate.opsForValue().set(RedisKey.USR_EMAIL+email,s);
        redisTemplate.expire(RedisKey.USR_EMAIL+email,30, TimeUnit.MINUTES); //设置三十分钟失效
        return s;
    }

    public String getEmailCode(String email){
        return (String) redisTemplate.opsForValue().get(RedisKey.USR_EMAIL+email);
    }

}
