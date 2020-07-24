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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@CacheConfig(cacheNames = "comment")
public class RedisCommentManager {

    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private RedisUserManager redisUserManager;

    @Autowired
    private CommentMapper mapper;

    @Autowired
    private RedisTemplate redisTemplate;


    private List<Comment> getComments(List<Comment> comments){
        for (Comment comment : comments) {
            comment.setUser(redisUserManager.get(comment.getUid()));  //给首部评论栏设置user
            comment.setChilds(getChildComments(comment));        //给首部评论栏设置
        }
        return comments;
    }

    private List<Comment> getChildComments(Comment comment){
        List<Comment> comments=new ArrayList<>();
        List<Comment> childs=mapper.listByCid(comment.getId());  //查出其所有的子评论
        for (Comment child : childs) {
            child.setParentVisitorName(mapper.findParentName(child.getParentID()));
            child.setUser(redisUserManager.get(child.getUid()));
            comments.add(child);
            comments.addAll(getChildComments(child));
        }
        return comments;
    }

    /**
     * 新增这个评论并将原来该文章的评论清空,全部重新载入
     * @param comment
     */
//    @Caching(evict = {
//            @CacheEvict(key = "'countOfArticle'+#comment.getAid()"),
//            @CacheEvict(key = "'countOfComment'+#comment.getAid()"),
//            @CacheEvict(key = "'lastComment'"),
//    })
    public void add(Comment comment) {
        mapper.insert(comment);
        Set<String> keys = redisTemplate.keys(RedisKey.COMMENT+"aid_" + comment.getAid() + "*");
        redisTemplate.delete(keys);
        redisTemplate.delete(RedisKey.COUNTOF_ARTICLE+comment.getAid());
        redisTemplate.delete(RedisKey.COUNTOF_COMMENT+comment.getAid());
        redisTemplate.delete(RedisKey.LAST_COMMENT);
    }

    /**
     * 递归级联删除一个评论及其子评论
     * @param id
     */
  //  @CacheEvict(allEntries = true)
    public void delete(Comment comment) {
        LOG.info("{}被删除",comment.getId());
        Set<String> keys = redisTemplate.keys(RedisKey.COMMENT+"aid_" + comment.getAid() + "*");
        redisTemplate.delete(keys);
        redisTemplate.delete(RedisKey.COUNTOF_ARTICLE+comment.getAid());
        redisTemplate.delete(RedisKey.COUNTOF_COMMENT+comment.getAid());
        redisTemplate.delete(RedisKey.LAST_COMMENT);
        for (Comment item : mapper.listByCid(comment.getId())) {
            this.delete(item);
        }
        mapper.deleteByPrimaryKey(comment.getId());
    }

//    @Cacheable(key = "'aid_'.concat(#a0)+','+'start_'.concat(#a1)", unless="#result == null")
    public List<Comment> findByAid(Integer aid, Integer start, Integer count) {
        if(!redisTemplate.hasKey(RedisKey.COMMENT+"aid_" +aid +"start_"+start)){
            LOG.info("文章{}评论缓存未命中",aid);
            List<Comment> comments=mapper.findByAid(aid,start,count);
            redisTemplate.opsForValue().set(RedisKey.COMMENT+"aid_" +aid +"start_"+start,getComments(comments));
        }
        return (List<Comment>) redisTemplate.opsForValue().get(RedisKey.COMMENT+"aid_" +aid +"start_"+start);
    }

  //  @Cacheable(key = "'countOfArticle'.concat(#a0)",unless="#result == null")
    public Integer countOfArticle(Integer aid) {
        if(!redisTemplate.hasKey(RedisKey.COUNTOF_ARTICLE+aid)){
            LOG.info("文章{}评论数缓存未命中",aid);
            redisTemplate.opsForValue().set(RedisKey.COUNTOF_ARTICLE+aid, mapper.countOfArticle(aid));
        }
        return (Integer) redisTemplate.opsForValue().get(RedisKey.COUNTOF_ARTICLE+aid);
    }

  //  @Cacheable(key = "'countOfComment'.concat(#a0)", unless="#result == null")
    public Integer countOfComment(Integer aid) {
        if(!redisTemplate.hasKey(RedisKey.COUNTOF_COMMENT+aid)){
            LOG.info("文章{}评论加回复数缓存未命中",aid);
            redisTemplate.opsForValue().set(RedisKey.COUNTOF_COMMENT+aid, mapper.countOfComment(aid));
        }
        return (Integer) redisTemplate.opsForValue().get(RedisKey.COUNTOF_COMMENT+aid);
    }

   // @Cacheable(key = "'lastComment'", unless="#result == null")
    public List<Comment> lastComment() {
        if(!redisTemplate.hasKey(RedisKey.LAST_COMMENT)){
            LOG.info("最新评论缓存未命中");
            List<Comment> comments=mapper.lastComment();
            for (Comment comment : comments) {
                comment.setUser(redisUserManager.get(comment.getUid()));
            }
            redisTemplate.opsForValue().set(RedisKey.LAST_COMMENT, comments);
        }
        return (List<Comment>) redisTemplate.opsForValue().get(RedisKey.LAST_COMMENT);
    }


    public void deleteCids(List<Comment> comments) {
        for (Comment comment : comments) {
            this.delete(comment);
        }
    }

    @CacheEvict(allEntries = true)
    public void evit(){
        LOG.info(new Date()+"定时清除所有评论缓存");
    }

}
