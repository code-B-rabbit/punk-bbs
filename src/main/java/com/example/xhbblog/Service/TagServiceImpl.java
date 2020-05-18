package com.example.xhbblog.Service;


import com.example.xhbblog.mapper.TagMapper;
import com.example.xhbblog.pojo.Tag;
import com.example.xhbblog.pojo.TagExample;
import com.example.xhbblog.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "tag")
@EnableScheduling
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);


    @Override
    @CacheEvict(allEntries = true)
    public Tag add(Tag tag) {
        tagMapper.insertSelective(tag);
        return tag;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        tagMapper.deleteByPrimaryKey(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Tag update(Tag tag) {
        tagMapper.updateByPrimaryKey(tag);
        return tag;
    }

    @Override
    public Tag get(Integer id) {
        return tagMapper.selectByPrimaryKey(id);
    }


    @Override
    @Cacheable(key = "getMethodName()")
    public List<Tag> list() {
        return tagMapper.list();
    }

    @Override
    public List<Tag> listBySort() {
        List<Tag> list = tagMapper.list();
        list.sort((a, b) -> a.getNumbersOfBlog() - b.getNumbersOfBlog());
        return list;     //这里考虑到博客的数量可能经常变动
    }

    @Scheduled(cron = "0 0 1 * * ?")  //每天的凌晨一点
    @CacheEvict(allEntries = true)
    public void evit()
    {
        LOG.info(new Date()+"定时清除所有标签缓存");
    }

}
