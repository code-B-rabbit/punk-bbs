package com.example.xhbblog.service.impl;

import com.example.xhbblog.pojo.Thumb;
import com.example.xhbblog.service.ThumbsService;
import com.example.xhbblog.manager.RedisThumbManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional(isolation= Isolation.READ_COMMITTED)
@EnableScheduling
public class ThumbsServiceImpl implements ThumbsService {



    private Logger LOG= LoggerFactory.getLogger(ThumbsServiceImpl.class);

    @Autowired
    private RedisThumbManager redisThumbManager;

    /**
     * 点赞业务
     * @param thumbs
     */
    @Override
    public void insert(Thumb thumbs) {
        LOG.info("新增点赞{}",thumbs);
        redisThumbManager.insert(thumbs);
    }

    /**
     * 取消点赞业务
     * @param aid
     * @param address
     */
    @Override
    public void deleteThumb(Integer aid, String address) {
        LOG.info("删除文章:{}的来自ip地址{}的点赞",aid,address);
        redisThumbManager.deleteThumb(aid,address);
    }


    /**
     * 每天两点和凌晨一点将点赞缓存持久化进数据库
     */
    @PostConstruct
    @Scheduled(cron = "0 0 1,14 * * ?")
    @Override
    public void redisDataToMySQL() {
        redisThumbManager.redisThumbDataToMySQL();
    }


}
