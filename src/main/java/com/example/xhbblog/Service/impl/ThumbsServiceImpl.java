package com.example.xhbblog.Service.impl;

import com.example.xhbblog.Service.ThumbsService;
import com.example.xhbblog.mapper.ThumbsMapper;
import com.example.xhbblog.pojo.Thumbs;
import com.example.xhbblog.pojo.ThumbsExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@EnableScheduling
public class ThumbsServiceImpl implements ThumbsService {

    @Autowired
    private ThumbsMapper thumbsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private Logger logger= LoggerFactory.getLogger(ThumbsServiceImpl.class);

    /**
     * 这里前面的操作是防止持久化时访问的情况
     * @param thumbs
     */
    @Override
    public void insert(Thumbs thumbs) {
        synchronized (this){
            if(!redisTemplate.hasKey("thumbs:"+thumbs.getAid())){
                List<String> addrList = thumbsMapper.ipOfAid(thumbs.getAid());
                if(addrList.size()!=0){
                    redisTemplate.opsForSet().add("thumbs:"+thumbs.getAid(),addrList);
                }
            }
            redisTemplate.opsForSet().add("thumbs:"+thumbs.getAid(),thumbs.getAddress());
        }
    }

    /**
     * 这里前面的操作是防止持久化时访问的情况
     * @param aid
     * @param address
     */
    @Override
    public void deleteThumb(Integer aid, String address) {
        synchronized (this){
            if(countOf(aid)!=null){
                redisTemplate.opsForSet().remove("thumbs:"+aid,address);
                //redis的Set清空后这个键值就被自动设为零
            }
        }
    }

    /**
     * 某文章的点赞数
     * @param aid
     * @return
     */
    @Override
    public Integer countOf(Integer aid) {
        synchronized (this){
            if(!redisTemplate.hasKey("exist:"+aid)){             //某文章的点赞缓存是否存在
                List<String> thumbs = thumbsMapper.ipOfAid(aid);
                if(thumbs.size()!=0){
                    for (String thumb : thumbs) {
                        redisTemplate.opsForSet().add("thumbs:"+aid,thumb);
                    }
                }
                redisTemplate.opsForValue().set("exist:"+aid,true);
            }
            return redisTemplate.opsForSet().size("thumbs:"+aid).intValue();
        }
    }

    @Override
    public Boolean isThumb(Integer aid, String address) {
        synchronized (this){
            //System.out.println(redisTemplate.opsForSet().members("thumbs:" + aid));
            Set members=redisTemplate.opsForSet().members("thumbs:" + aid);
            for (Object member : members) {
                if(member.equals(address)){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 每天两点和凌晨一点将点赞缓存持久化进数据库
     */
    @Scheduled(cron = "0 0 1,14 * * ?")
    public void redisDataToMySQL() {
        synchronized (this){
            logger.info("time:{}，开始执行Redis数据持久化到MySQL任务", LocalDateTime.now());
            //1.更新文章总的点赞数
            Set<String> keys = redisTemplate.keys("exist:*");
            for (String key : keys) {
                Integer aid=Integer.parseInt(key.substring(6));
                List<String> thumbs = thumbsMapper.ipOfAid(aid);
                if(thumbs.size()!=0){
                    for (String thumb : thumbs) {
                        redisTemplate.opsForSet().add("mysql:"+aid,thumb);
                    }
                }
                redisTemplate.opsForSet().intersectAndStore("mysql:"+aid,"thumbs:"+aid,"union");//取到一个并集
                System.out.println(redisTemplate.opsForSet().members("union").size());
                Set<String> toRemove = redisTemplate.opsForSet().difference( "mysql:" + aid,"union");//mysql中与并集不相符的全部删掉
                //System.out.println(toRemove.size());
                removeOld(toRemove,aid);
                Set<String> toAdd=redisTemplate.opsForSet().difference("thumbs:"+aid,"union");
                addId(toAdd,aid);

                redisTemplate.delete(key);
                redisTemplate.delete("mysql:"+aid);
                redisTemplate.delete("thumbs:"+aid);
                redisTemplate.delete("union");
            }
            logger.info("time:{}，结束执行Redis数据持久化到MySQL任务", LocalDateTime.now());
        }
    }

    /**
     * 删除所有取消赞的项
     * @param toRemove
     * @param aid
     */
    private void removeOld(Set<String> toRemove,Integer aid){
        for (String s : toRemove) {
            thumbsMapper.deleteThumb(aid,s);
        }
    }

    private void addId(Set<String> toAdd,Integer aid){
        for (String s : toAdd) {
            Thumbs thumbs=new Thumbs();
            thumbs.setAddress(s);
            thumbs.setAid(aid);
            thumbsMapper.insert(thumbs);
        }
    }

}
