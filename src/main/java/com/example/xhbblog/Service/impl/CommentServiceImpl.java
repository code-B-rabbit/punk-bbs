package com.example.xhbblog.Service.impl;

import com.example.xhbblog.Service.CommentService;
import com.example.xhbblog.mapper.CommentMapper;
import com.example.xhbblog.pojo.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Service
@Transactional
@CacheConfig(cacheNames = "comment")
@EnableScheduling           //开启定时器
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private CommentMapper mapper;

    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'countOfArticle'+#comment.getAid()"),
            @CacheEvict(key = "'countOfComment'+#comment.getAid()"),
            @CacheEvict(key = "'lastComment'"),
    })
    public void add(Comment comment) {
        mapper.insert(comment);
        Set<String> keys = redisTemplate.keys("comment::" + comment.getAid() + "*");
        redisTemplate.delete(keys);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Comment comment) {
        mapper.updateByPrimaryKey(comment);
    }

    @Override
    public Comment get(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Comment> list() {
        return mapper.list();
    }

    @Override
    @Cacheable(key = "#aid+','+#start")
    public List<Comment> findByAid(Integer aid,Integer start,Integer count) {
        LOG.info("文章{}评论缓存未命中",aid);
        List<Comment> comments=mapper.findByAid(aid,start,count);
        return getComments(comments);
    }

    @Override
    public List<Comment> listByAid(Integer aid) {
        return mapper.listByAid(aid);       //给后台查看评论使用的接口
    }

    List<Comment> getComments(List<Comment> comments)
    {
        ArrayList<Comment> anw = new ArrayList<>();
        for(int i=0;i<comments.size();i++)
        {
            Comment comment=comments.get(i);
            comment.setChilds(getChildComments(comment));  //全都变为"直接评论方的子节点"
            anw.add(comment);
        }
        return anw;
    }

    List<Comment> getChildComments(Comment comment)
    {
        List<Comment> anw=new ArrayList<>();
        for(int i=0;i<comment.getChilds().size();i++)
        {
            Comment comment1=comment.getChilds().get(i);
            anw.add(comment1);
            if(comment1.getChilds()!=null&&comment1.getChilds().size()!=0)
            {
                anw.addAll(getChildComments(comment1));
            }
        }
        return anw;
    }

    @Override
    @Cacheable(key = "'countOfArticle'+#aid")
    public Integer countOfArticle(Integer aid) {
        LOG.info("文章{}评论数缓存未命中",aid);
        return mapper.countOfArticle(aid);
    }

    @Override
    @Cacheable(key = "'countOfComment'+#aid")
    public Integer countOfComment(Integer aid) {
        LOG.info("文章{}评论加回复数缓存未命中",aid);
        return mapper.countOfComment(aid);
    }

    @Override
    @Cacheable(key = "'lastComment'")
    public List<Comment> lastComment() {
        LOG.info("最新评论缓存未命中");
        return mapper.lastComment();
    }

    @Override
    public List<Comment> listByUid(Integer aid) {
        return mapper.listByUid(aid);       //给后台查看评论使用的接口
    }

    @Scheduled(cron = "0 0 10,14,16 * * ?")  //每天上午10点，下午2点，4点
    @CacheEvict(allEntries = true)
    public void evit()
    {
        LOG.info(new Date()+"定时清除所有评论缓存");
    }

}
