package com.example.xhbblog.manager;

import com.example.xhbblog.Service.impl.CommentServiceImpl;
import com.example.xhbblog.mapper.CommentMapper;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.utils.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@Transactional
@CacheConfig(cacheNames = "comment")
public class RedisCommentManager {

    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentMapper mapper;

    @Autowired
    private RedisTemplate redisTemplate;

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

    /**
     * 新增这个评论并将原来该文章的评论清空,全部重新载入
     * @param comment
     */
    @Caching(evict = {
            @CacheEvict(key = "'countOfArticle'+#comment.getAid()"),
            @CacheEvict(key = "'countOfComment'+#comment.getAid()"),
            @CacheEvict(key = "'lastComment'"),
    })
    public void add(Comment comment) {
        mapper.insert(comment);
        Set<String> keys = redisTemplate.keys(RedisKey.COMMENT + comment.getAid() + "*");
        redisTemplate.delete(keys);
    }

    /**
     * 递归级联删除一个评论及其子评论
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        LOG.info("{}被删除",id);
        for (Comment comment : mapper.deleteDfs(id)) {
            this.delete(comment.getId());
        }
        mapper.deleteByPrimaryKey(id);
    }

    @Cacheable(key = "#aid+','+#start")
    public List<Comment> findByAid(Integer aid, Integer start, Integer count) {
        LOG.info("文章{}评论缓存未命中",aid);
        List<Comment> comments=mapper.findByAid(aid,start,count);
        return getComments(comments);
    }

    @Cacheable(key = "'countOfArticle'+#aid")
    public Integer countOfArticle(Integer aid) {
        LOG.info("文章{}评论数缓存未命中",aid);
        return mapper.countOfArticle(aid);
    }

    @Cacheable(key = "'countOfComment'+#aid")
    public Integer countOfComment(Integer aid) {
        LOG.info("文章{}评论加回复数缓存未命中",aid);
        return mapper.countOfComment(aid);
    }

    @Cacheable(key = "'lastComment'")
    public List<Comment> lastComment() {
        LOG.info("最新评论缓存未命中");
        return mapper.lastComment();
    }

    @CacheEvict(allEntries = true)
    public void deleteCids(List<Integer> cids) {
        for (Integer cid : cids) {
            this.delete(cid);
        }
    }

    @CacheEvict(allEntries = true)
    public void evit(){
        LOG.info(new Date()+"定时清除所有评论缓存");
    }

}
