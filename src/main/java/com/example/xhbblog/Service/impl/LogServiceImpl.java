package com.example.xhbblog.Service.impl;

import com.example.xhbblog.Service.LogService;
import com.example.xhbblog.mapper.LogMapper;
import com.example.xhbblog.pojo.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "logsOfToday")
@EnableScheduling
public class LogServiceImpl implements LogService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LogMapper logMapper;

    @Scheduled(cron = "0 0 23 * * ?")
    @CacheEvict(allEntries = true)
    @Override
    public void writeDate() {   //将每日的访问量持久化到数据库中
        Long size = redisTemplate.opsForHyperLogLog().size("VisitOfToday");
        Log log = new Log();
        log.setCreateTime(new Date());
        log.setVisit(size);
        logMapper.insert(log);
        redisTemplate.delete("VisitOfToday");
    }

    @Override
    public void checkAndAdd(String ipAddr) {
        redisTemplate.opsForHyperLogLog().add("VisitOfToday",ipAddr);
    }

    @Override
    @Cacheable(key = "'findLastLogs'")
    public List<Log> findLastLogs() {
        return logMapper.findLastLogs();
    }
}
