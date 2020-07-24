package com.example.xhbblog.Service.impl;

import com.example.xhbblog.Service.LogService;
import com.example.xhbblog.mapper.LogMapper;
import com.example.xhbblog.pojo.Log;
import com.example.xhbblog.utils.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;


@Lazy(false)
@Service
@Transactional(isolation= Isolation.READ_COMMITTED)
@EnableScheduling
@CacheConfig(cacheNames = "visitLog")
public class LogServiceImpl implements LogService {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LogMapper logMapper;


    @Scheduled(cron = "0 59 23 * * ?")
    @CacheEvict
    @Override
    public void writeDate() {   //将每日的访问量持久化到数据库中
        Long size = redisTemplate.opsForHyperLogLog().size(RedisKey.VISITOFTODAY);
        Log log = new Log();
        log.setCreateTime(new Date());
        log.setVisit(size);
        logger.info("将{}持久化到数据库中",log);
        logMapper.insert(log);
        redisTemplate.delete(RedisKey.VISITOFTODAY);
        redisTemplate.delete(RedisKey.LAST_LOGS);
    }

    @Override
    public void checkAndAdd(String ipAddr) {
        redisTemplate.opsForHyperLogLog().add(RedisKey.VISITOFTODAY,ipAddr);
    }

    @Override
    @Cacheable(value = "'findLastLogs'")
    public List<Log> findLastLogs() {
        return logMapper.findLastLogs();
    }

}
