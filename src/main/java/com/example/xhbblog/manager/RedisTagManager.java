package com.example.xhbblog.manager;

import com.example.xhbblog.Service.impl.TagServiceImpl;
import com.example.xhbblog.mapper.TagMapper;
import com.example.xhbblog.pojo.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@CacheConfig(cacheNames = "tag")
@Transactional(isolation= Isolation.READ_COMMITTED)
public class RedisTagManager {


    @Autowired
    private TagMapper tagMapper;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());


    @CacheEvict(allEntries = true)
    public Tag add(Tag tag) {
        tagMapper.insertSelective(tag);
        return tag;
    }

    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        tagMapper.deleteByPrimaryKey(id);
    }


    @CacheEvict(allEntries = true)
    public Tag update(Tag tag) {
        tagMapper.updateByPrimaryKey(tag);
        return tag;
    }


    @Cacheable(key = "'tag_'.concat(#a0)")
    public Tag get(Integer id) {
        LOG.info(id+"标签缓存未命中");
        return tagMapper.selectByPrimaryKey(id);
    }



    @Cacheable(key = "getMethodName()")
    public List<Tag> list() {
        return tagMapper.list();
    }


    public Integer count() {
        return tagMapper.count();
    }


    @CacheEvict(allEntries = true)
    public void evit()
    {
        LOG.info(new Date()+"定时清除所有标签缓存");
    }
}
