package com.example.xhbblog.service.impl;

import com.example.xhbblog.service.FriendLyLinkService;
import com.example.xhbblog.mapper.FriendlyLinkMapper;
import com.example.xhbblog.pojo.FriendlyLink;
import com.example.xhbblog.pojo.FriendlyLinkExample;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(isolation= Isolation.READ_COMMITTED)
@Slf4j
public class FrientLyLinkServiceImpl implements FriendLyLinkService {

    @Autowired
    private FriendlyLinkMapper mapper;

    @Override
    public void add(FriendlyLink friendlyLink) {
        log.info("新增友链{}",friendlyLink);
        mapper.insert(friendlyLink);
    }

    @Override
    public void delete(Integer id) {
        log.info("删除友链{}",id);
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(FriendlyLink friendlyLink) {
        log.info("修改友链{}",friendlyLink);
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
}
