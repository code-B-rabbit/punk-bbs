package com.example.xhbblog.manager;


import com.example.xhbblog.mapper.UserMapper;
import com.example.xhbblog.message.WebSocketMessage;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.utils.RedisKey;
import org.apache.shiro.session.Session;
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

import java.io.Serializable;
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


    public User findByName(String name) {
        if(redisTemplate.hasKey(RedisKey.USR_NAME+name)==false){
            logger.info("匿名缓存未命中");
            redisTemplate.opsForValue().set(RedisKey.USR_NAME+name,userMapper.findByName(name));
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

    public void sendMessageTo(Integer uid,String msg){
        WebSocketMessage message = new WebSocketMessage(uid,msg);
        redisTemplate.convertAndSend(RedisKey.WEBSOCKET_CHANNEL,message);
        redisTemplate.opsForList().leftPush(RedisKey.NOT_READ_COMMENT_LIST+uid,msg);  //放入redis list对象里去
        redisTemplate.expire(RedisKey.NOT_READ_COMMENT_LIST+uid,7,TimeUnit.DAYS);
    }

    //消息关闭时为设置已读的消息,设置七天的过期时间
    public void addMessages(Integer uid,String []messages){
        redisTemplate.opsForList().rightPushAll(RedisKey.NOT_READ_COMMENT_LIST+uid,messages);
        redisTemplate.expire(RedisKey.NOT_READ_COMMENT_LIST+uid,7,TimeUnit.DAYS);
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

    public Serializable loadShiroSession(Integer uid){
        return (Serializable) redisTemplate.opsForValue().get(RedisKey.SHIRO_SESSION_ONLINE+uid);
    }

    public void putShiroSession(Integer uid,Serializable sessionId){
        redisTemplate.opsForValue().set(RedisKey.SHIRO_SESSION_ONLINE+uid,sessionId);
        redisTemplate.expire(RedisKey.SHIRO_SESSION_ONLINE+uid,2,TimeUnit.HOURS);
    }

    public  void deleteShiroSession(Integer uid){
        redisTemplate.delete(RedisKey.SHIRO_SESSION_ONLINE+uid);
    }
}
