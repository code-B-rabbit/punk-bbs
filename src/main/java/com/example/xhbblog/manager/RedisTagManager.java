package com.example.xhbblog.manager;

import com.example.xhbblog.mapper.TagMapper;
import com.example.xhbblog.pojo.Tag;
import com.example.xhbblog.utils.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RedisTagManager {

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private TagMapper tagMapper;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());


    public void deleteAllTagCache(){
        redisTemplate.delete(RedisKey.TAG+"*");
    }


    public Tag add(Tag tag) {
        tagMapper.insertSelective(tag);
        deleteAllTagCache();
        return tag;
    }


    public void delete(Integer id) {
        tagMapper.deleteByPrimaryKey(id);
        deleteAllTagCache();
    }



    public Tag update(Tag tag) {
        tagMapper.updateByPrimaryKey(tag);
        deleteAllTagCache();
        return tag;
    }


    public Tag get(Integer id) {
        if(!redisTemplate.hasKey(RedisKey.TAG+id)){
            redisTemplate.opsForValue().set(RedisKey.TAG+id,tagMapper.selectByPrimaryKey(id));
        }
        LOG.info(id+"标签缓存未命中");
        return (Tag) redisTemplate.opsForValue().get(RedisKey.TAG+id);
    }



    public List<Tag> list() {
        if(!redisTemplate.hasKey(RedisKey.TAG_LIST)){
            redisTemplate.opsForValue().set(RedisKey.TAG_LIST,tagMapper.list());
        }
        LOG.info("标签列表缓存未命中");
        return (List<Tag>) redisTemplate.opsForValue().get(RedisKey.TAG_LIST);
    }


    public Integer count() {
        return tagMapper.count();
    }


    public void evit()
    {
        LOG.info(new Date()+"定时清除所有标签缓存");
        deleteAllTagCache();
    }
}
