package com.example.xhbblog.manager;

import com.example.xhbblog.mapper.ThumbsMapper;
import com.example.xhbblog.pojo.Thumbs;
import com.example.xhbblog.utils.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@Transactional
public class RedisThumbManager {


    private Logger LOG= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThumbsMapper thumbsMapper;

    /**
     * 某文章点赞数
     * @param aid
     * @return
     */
    public Integer thumbCountOf(Integer aid) {
        if(!redisTemplate.hasKey(RedisKey.THUMB_EXIST+aid)){             //某文章的点赞缓存是否存在
            LOG.info("文章::{}点赞缓存未命中",aid);
            List<String> thumbs = thumbsMapper.ipOfAid(aid);
            if(thumbs.size()!=0){
                for (String thumb : thumbs) {
                    redisTemplate.opsForSet().add(RedisKey.THUMBS_SET+aid,thumb);
                }
            }
            redisTemplate.opsForValue().set(RedisKey.THUMB_EXIST+aid,true);
        }
        return redisTemplate.opsForSet().size(RedisKey.THUMBS_SET+aid).intValue();
    }

    /**
     * 某文章是否被点赞
     */
    public Boolean artIsThumb(Integer aid, String address) {
        synchronized (this){
            Set members=redisTemplate.opsForSet().members(RedisKey.THUMBS_SET + aid);
            return members.contains(address);
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

    /**
     * 取消赞
     * @param aid
     * @param address
     */
    public void deleteThumb(Integer aid, String address) {
        synchronized (this){
            LOG.info("文章::{}被取消点赞",aid);
            if(this.thumbCountOf(aid)!=null){
                redisTemplate.opsForSet().remove(RedisKey.THUMBS_SET+aid,address);
                //redis的Set清空后这个键值就被自动设为零
            }
        }
    }

    /**
     * 点赞
     * @param thumbs
     */
    public void insert(Thumbs thumbs) {
        synchronized (this){
            LOG.info("文章::{}被点赞",thumbs.getAid());
            if(!redisTemplate.hasKey(RedisKey.THUMBS_SET+thumbs.getAid())){
                List<String> addrList = thumbsMapper.ipOfAid(thumbs.getAid());
                if(addrList.size()!=0){
                    redisTemplate.opsForSet().add(RedisKey.THUMBS_SET +thumbs.getAid(),addrList);
                }
            }
            redisTemplate.opsForSet().add(RedisKey.THUMBS_SET+thumbs.getAid(),thumbs.getAddress());
        }
    }


    /**
     * 将点赞数据持久化到数据库
     */
    public void redisThumbDataToMySQL() {
        synchronized (this){
            LOG.info("time:{}，开始执行Redis数据持久化到MySQL任务", LocalDateTime.now());
            //1.更新文章总的点赞数
            Set<String> keys = redisTemplate.keys(RedisKey.THUMB_EXIST+"*");
            for (String key : keys) {
                Integer aid=Integer.parseInt(key.substring(6));
                List<String> thumbs = thumbsMapper.ipOfAid(aid);
                if(thumbs.size()!=0){
                    for (String thumb : thumbs) {
                        redisTemplate.opsForSet().add(RedisKey.THUMB_TOMYSQL+aid,thumb);
                    }
                }
                redisTemplate.opsForSet().intersectAndStore(RedisKey.THUMB_TOMYSQL+aid, RedisKey.THUMBS_SET+aid, RedisKey.THUMB_SET_UNION);//取到一个并集
                Set<String> toRemove = redisTemplate.opsForSet().difference( RedisKey.THUMB_TOMYSQL + aid, RedisKey.THUMB_SET_UNION);//mysql中与并集不相符的全部删掉
                removeOld(toRemove,aid);
                Set<String> toAdd=redisTemplate.opsForSet().difference(RedisKey.THUMBS_SET+aid, RedisKey.THUMB_SET_UNION);
                addId(toAdd,aid);

                redisTemplate.delete(key);
                redisTemplate.delete(RedisKey.THUMB_TOMYSQL+aid);
                redisTemplate.delete(RedisKey.THUMBS_SET+aid);
                redisTemplate.delete(RedisKey.THUMB_SET_UNION);
            }
            LOG.info("time:{}，结束执行Redis数据持久化到MySQL任务", LocalDateTime.now());
        }
    }
}
