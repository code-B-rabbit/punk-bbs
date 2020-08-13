package com.example.xhbblog.manager;

import com.example.xhbblog.mapper.ThumbMapper;
import com.example.xhbblog.pojo.Thumb;
import com.example.xhbblog.utils.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 点赞缓存实现:将一个一个文章用户的点赞状态放进redis HashMap中
 * 用户点赞一次,将用户ip当做加到HashMap中设置值为1
 * 取消则设置值为0
 *
 */
@Component
public class RedisThumbManager {


    private Logger LOG= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThumbMapper thumbsMapper;

    /**
     * 某文章点赞数
     * @param aid
     * @return
     */
    public Integer thumbCountOf(Integer aid) {
        Integer cnt = 0;
        if(!redisTemplate.hasKey(RedisKey.THUMB_CNT+aid)){             //某文章的点赞缓存是否存在
            LOG.info("{}点赞数未命中,将相应点赞项放入缓存",aid);
            List<String> ips= thumbsMapper.ipOfAid(aid);
            for (String ip : ips) {
                redisTemplate.opsForHash().put(RedisKey.THUMB_AID_MAP+aid,ip,1);
                cnt++;
            }
            redisTemplate.opsForValue().set(RedisKey.THUMB_CNT+aid,cnt);
        }
        return (Integer) redisTemplate.opsForValue().get(RedisKey.THUMB_CNT+aid);
    }

    /**
     * 某文章是否被点赞
     * 查看维护的哈希表中的值
     */
    public Boolean artIsThumb(Integer aid, String address) {
        Integer status= (Integer) redisTemplate.opsForHash().get(RedisKey.THUMB_AID_MAP+aid,address);
        if(status==null){
            return false;
        }
        return status==1;
    }


    /**
     * 取消赞
     * @param aid
     * @param address
     */
    public void deleteThumb(Integer aid, String address) {
        LOG.info("文章::{}被点赞",aid);
        redisTemplate.opsForValue().decrement(RedisKey.THUMB_CNT+aid,1);
        redisTemplate.opsForHash().put(RedisKey.THUMB_AID_MAP+aid,address,0);
    }

    /**
     * 点赞
     * @param thumbs
     */
    public void insert(Thumb thumbs) {
        LOG.info("文章::{}被点赞",thumbs.getAid());
        redisTemplate.opsForValue().increment(RedisKey.THUMB_CNT+thumbs.getAid(),1);
        redisTemplate.opsForHash().put(RedisKey.THUMB_AID_MAP+thumbs.getAid(),thumbs.getAddress(),1);
    }


    /**
     * 将点赞数据持久化到数据库
     * 首先加锁,防止持久化过程中有点赞业务,部分持久化数据丢失
     * 再将所有的已访问的点赞缓存(通过查看是否已经缓存点赞计时器)全部持久化一下
     * 若为0则调动mapper层判断是否存在并删除,若为1则插入
     */
    public void redisThumbDataToMySQL() {
        synchronized (this){
            LOG.info("time:{}，开始执行Redis数据持久化到MySQL任务", LocalDateTime.now());
            //1.更新文章总的点赞数
            Set<String> keys = redisTemplate.keys(RedisKey.THUMB_CNT+"*");
            for (String key : keys) {
                Integer aid=Integer.parseInt(key.substring(key.indexOf(RedisKey.THUMB_CNT)+RedisKey.THUMB_CNT.length()));
                Map<String,Integer> entries = redisTemplate.opsForHash().entries(RedisKey.THUMB_AID_MAP + aid);
                for (String address : entries.keySet()) {
                    if(entries.get(address)==1){
                        thumbsMapper.insertThumbSelective(aid,address);
                    }else if(entries.get(address)==0){
                        thumbsMapper.deleteThumb(aid,address);
                    }
                }
                redisTemplate.delete(RedisKey.THUMB_AID_MAP + aid);
                redisTemplate.delete(key);
            }
            LOG.info("time:{}，结束执行Redis数据持久化到MySQL任务", LocalDateTime.now());
        }
    }
}
