package com.example.xhbblog.service.impl;


import com.example.xhbblog.service.TagService;
import com.example.xhbblog.manager.RedisTagManager;
import com.example.xhbblog.mapper.TagMapper;
import com.example.xhbblog.pojo.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(isolation= Isolation.READ_COMMITTED)
@EnableScheduling
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RedisTagManager redisTagManager;

    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);


    @Override
    public Tag add(Tag tag) {
        LOG.info("新增标签{},ID：{}",tag.getName(),tag.getId());
        return redisTagManager.add(tag);
    }

    @Override
    public void delete(Integer id) {
        LOG.info("删除标签{}",id);
        redisTagManager.delete(id);
    }

    @Override
    public Tag update(Tag tag) {
        LOG.info("更新标签{}",tag.getId());
        return redisTagManager.update(tag);
    }

    @Override
    public Tag get(Integer id) {
        LOG.info("查询标签{}",id);
        return redisTagManager.get(id);
    }


    @Override
    public List<Tag> list() {
        LOG.info("查询全部标签");
        return redisTagManager.list();
    }

    @Override
    public List<Tag> listBySort() {
        List<Tag> list = tagMapper.list();
        list.sort((a, b) -> a.getNumbersOfBlog() - b.getNumbersOfBlog());
        return list;     //这里考虑到博客的数量可能经常变动
    }

    @Override
    public Integer count() {
        LOG.info("查询标签数量");
        return tagMapper.count();
    }

    @Scheduled(cron = "0 0 1 * * ?")  //每天的凌晨一点
    public void evit()
    {
        redisTagManager.evit();
    }

}
