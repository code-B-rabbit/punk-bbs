package com.example.xhbblog.Service.impl;


import com.example.xhbblog.manager.*;
import com.example.xhbblog.Service.*;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.pojo.*;
import com.example.xhbblog.utils.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Transactional(isolation= Isolation.READ_COMMITTED)
@EnableScheduling
public class ArticleServiceImpl implements ArticleService {


    private static final Logger LOG = LoggerFactory.getLogger(ArticleServiceImpl.class);


    @Autowired
    private ArticleMapper articleMapper;


    @Autowired
    private RedisUserManager redisUserManager;

    @Autowired
    private RedisTagManager redisTagManager;

    @Autowired
    private RedisArticleManager redisArticleManager;

    @Autowired
    private RedisThumbManager redisThumbManager;

    @Autowired
    private RedisCommentManager redisCommentManager;

    @Override
    public void add(ArticleWithBLOBs article) {
        articleMapper.insert(article);
        redisArticleManager.deleteKey(RedisKey.LAST_ARTICLE);
    }


    /**
     * 删除文章思路:删除对应的文章点赞,删除对应的文章,删除对应的文章点赞以及其对应缓存
     * 若为置顶项删除置顶
     * 更新最新访问和访问最多
     * 删除对应的评论
     * @param id
     * @param uid
     */
    @Override
    public void deleteArticle(Integer id, Integer uid) {
        redisArticleManager.deleteArticle(id,uid);     //从缓存/数据库中删除
    }


    @Override
    public void update(ArticleWithBLOBs article) {
        articleMapper.updateByPrimaryKeySelective(article);     //若为更改则不更新时间
        redisArticleManager.updateArt(article);
    }

    @Override
    public ArticleWithBLOBs get(Integer id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    private Integer getId(Object s)
    {
        String num=s.toString().substring(9);
        Integer id=Integer.parseInt(num);
        return id;
    }

    private Long getVisit(Double s)
    {
        return s.longValue();
    }


    public void setCount(Article article)
    {
        article.setThumbsCount(redisThumbManager.thumbCountOf(article.getId()));
    }

    public void setThumb(Article item,String address)
    {
        item.setThumb(redisThumbManager.artIsThumb(item.getId(),address));
    }

    public void setTag(Article article)
    {
        article.setTag(redisTagManager.get(article.getTid()));
    }

    public void setVisit(Article article)
    {
        redisArticleManager.setVisit(article);
    }

    public void setUser(Article article){
        article.setUser(redisUserManager.get(article.getUid()));
    }

    public void setCommentSize(Article article)
    {
        article.setCommentSize(redisCommentManager.countOfComment(article.getId()));
    }

    /**
     * 若为条件所有则将相关的浏览,点赞缓存先持久化
     */
    public void checkOrder(Order order){
        if(order==null){
            return;
        }else if (order.getBy().equals("visit")){
            redisArticleManager.writeOut();
        }
    }

    /**
     * 查询相关的映射类
     * @param article
     */
    public void setUp(Article article,String address){
        setVisit(article);
        setCommentSize(article);
        setTag(article);
        setCount(article);
        setThumb(article,address);
        setUser(article);
    }

    public void setUp(Article article){
        setVisit(article);
        setCommentSize(article);
        setTag(article);
        setCount(article);
        setUser(article);
    }

    @Override
    public List<ArticleWithBLOBs> listByTid(Integer tid,Boolean published,Integer rank,Integer uid) {
        LOG.info("查询标签为{},是否发表为{}的文章内容",tid,published);
        Order order=Order.getOrder(rank);
        checkOrder(order);
        List<ArticleWithBLOBs> articles = articleMapper.findByTid(tid, published,order,uid);
        for (Article article : articles) {
            setUp(article);
        }
        return articles;
    }

    @Override
    public ArticleWithBLOBs findById(Integer id,String address)
    {
        LOG.info("查询文章{}",id);
        ArticleWithBLOBs article = articleMapper.findById(id);
        setUp(article,address);
        return article;
    }

    @Override
    public List<ArticleWithBLOBs> findByTid(Integer tid,String address,Boolean published) {
        LOG.info("查询标签为{},是否发表为{}的文章内容",tid,published);
        List<ArticleWithBLOBs> all = articleMapper.findByTid(tid,published,null,null);
        for (ArticleWithBLOBs item : all) {
            setUp(item,address);
        }
        return all;       //前台只向用户展示已经出版的
    }

    @Override
    public List<ArticleWithBLOBs> findByUid(Integer uid, String address, Boolean published) {
        LOG.info("查询标签为{},是否发表为{}的文章内容",uid,published);
        List<ArticleWithBLOBs> all = articleMapper.findAll(published,null,uid);
        for (ArticleWithBLOBs item : all) {
            setUp(item,address);
        }
        return all;
    }

    @Override
    public List<ArticleWithBLOBs> listAll(Boolean published,Integer rank,Integer uid) {
        LOG.info("查询是否发表为{}的全部文章",published);
        Order order=Order.getOrder(rank);
        checkOrder(order);
        List<ArticleWithBLOBs> articles = articleMapper.findAll(published,order,uid);
        for (Article article : articles) {
            setUp(article);
        }
        return articles;
    }

    @Override
    public List<TimeLine> timeLine() {
        return articleMapper.findTimeLines();
    }


    @Override
    public List<ArticleWithBLOBs> findAll(String address) {
        LOG.info("查询全部文章");
        List<ArticleWithBLOBs> all = articleMapper.findAll(true,null,null);
        for (ArticleWithBLOBs item : all) {
            setUp(item,address);
        }
        return all;       //前台只向用户展示已经出版的
    }


    @Override
    public List<ArticleWithBLOBs> findArticleLike(String key,String address) {
        LOG.info("以关键字{}搜索文章",key);
        List<ArticleWithBLOBs> articles=articleMapper.findArticleLike("%"+key+"%",true,null,null);
        for (ArticleWithBLOBs item: articles) {
            setUp(item,address);
        }
        return articles;
    }

    @Override
    public List<ArticleWithBLOBs> listArticleLike(String s,Boolean published,Integer rank,Integer uid) {
        LOG.info("以关键字{}搜索是否发表为{}的文章",s,published);
        Order order=Order.getOrder(rank);
        checkOrder(order);
        List<ArticleWithBLOBs> articles = articleMapper.findArticleLike("%" + s + "%", published,order,uid);
        for (Article article : articles) {
            setUp(article);
        }
        return articles;
    }

    @Override
    public List<Article> findLastestArticle() {
        LOG.info("查询最新发布文章");
        List<Article> lastestArticle=redisArticleManager.lastestArticle();
        for (Article article : lastestArticle) {
           setUp(article);
        }
        return lastestArticle;
    }

    @Override
    public Article getTitle(Integer id) {
        return articleMapper.getTitle(id);
    }

    //访问量最大的三篇文章
    @Override
    public List<Article> foreArticle() {
        LOG.info("查询访问量最大的三篇文章");
        List<Article> articles=new LinkedList<>();
        Set<ZSetOperations.TypedTuple<Object>> visits=redisArticleManager.foreArticle();
        for (ZSetOperations.TypedTuple<Object> visit : visits) {
                Article article = articleMapper.get(getId(visit.getValue()));
                article.setVisit(getVisit(visit.getScore()));
                setCommentSize(article);
                setUser(article);
                articles.add(article);
            }
        return articles;
    }


    @Override
    public void incr(ArticleWithBLOBs article) {
        redisArticleManager.incr(article);
    }

    @Override
    public List<ArticleWithBLOBs> topArts(String address,Boolean top,Integer uid) {
        List<ArticleWithBLOBs> anw=redisArticleManager.topArts(top,uid);
        for (ArticleWithBLOBs item : anw) {
            setUp(item,address);
        }
        return anw;
    }

    /**
     * 设为置顶
     * @param id
     */
    @Override
    public void topArticle(Integer id) {
        LOG.info("将文章{}设为置顶",id);
        articleMapper.topArticle(id);
        redisArticleManager.delTop();
    }

    /**
     * 取消置顶
     * @param id
     */
    @Override
    public void downArticle(Integer id) {
        LOG.info("将文章{}取消置顶",id);
        articleMapper.downArticle(id);
        redisArticleManager.delTop();
    }

    @Override
    public Integer count() {
        return articleMapper.count();
    }



    //排行榜相关业务
    @PostConstruct        //启动后初始化会执行一次
    @Scheduled(cron = "0 0 1 * * ?")  //每天的凌晨一点执行一次
    public void initCache(){
        LOG.info("对文章访问量进行定时写入");
        redisArticleManager.writeOut();
        redisArticleManager.readIn();
    }


}
