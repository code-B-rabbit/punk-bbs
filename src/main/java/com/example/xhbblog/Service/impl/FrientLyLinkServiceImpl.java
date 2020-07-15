package com.example.xhbblog.Service.impl;

import com.example.xhbblog.Service.FriendLyLinkService;
import com.example.xhbblog.mapper.FriendlyLinkMapper;
import com.example.xhbblog.pojo.FriendlyLink;
import com.example.xhbblog.pojo.FriendlyLinkExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "fls")
@EnableScheduling
public class FrientLyLinkServiceImpl implements FriendLyLinkService {

    @Autowired
    private FriendlyLinkMapper mapper;

    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    @CacheEvict(allEntries = true)
    public void add(FriendlyLink friendlyLink) {
        mapper.insert(friendlyLink);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void update(FriendlyLink friendlyLink) {
        mapper.updateByPrimaryKey(friendlyLink);
    }

    @Override
    public FriendlyLink get(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<FriendlyLink> list() {
        FriendlyLinkExample example=new FriendlyLinkExample();
        example.setOrderByClause("id desc");
        return mapper.selectByExample(example);
    }

    /**
     * 根据条件筛选查询出的友链列表
     * @param b
     * @return
     */
    @Override
    @Cacheable("fls")
    public List<FriendlyLink> ListOf(Boolean b) {
        FriendlyLinkExample example=new FriendlyLinkExample();
        example.setOrderByClause("id desc");
        if(b!=null) {
            example.createCriteria().andAllowedEqualTo(b);
        }
        return mapper.selectByExample(example);
    }

    @Override
    public Integer count() {
        return mapper.count();
    }

    @Scheduled(cron = "0 0 1 * * ?")  //每天的凌晨一点
    @CacheEvict(allEntries = true)
    public void evit()
    {
        LOG.info(new Date()+"定时清除所有友链缓存");
    }
}
