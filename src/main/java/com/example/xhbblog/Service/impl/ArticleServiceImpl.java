package com.example.xhbblog.Service.impl;

import com.example.xhbblog.Service.ArticleService;
import com.example.xhbblog.Service.CommentService;
import com.example.xhbblog.Service.TagService;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.mapper.ThumbsMapper;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.TimeLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Transactional
@CacheConfig(cacheNames = "article")
@EnableScheduling
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ZSetOperations zSet;

    private static final Logger LOG = LoggerFactory.getLogger(ArticleServiceImpl.class);



    @Autowired
    private ThumbsMapper thumbsMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    @Override
    public void add(ArticleWithBLOBs article) {
        articleMapper.insert(article);
        redisTemplate.delete("LastArticle");
        if(article.getTop()==true)
        {
            redisTemplate.delete("top");   //更新首页展示
        }
    }

    @Override
    public void remove(Integer id) {
        thumbsMapper.deleteThumbByAid(id);            //级联删除
        articleMapper.deleteByPrimaryKey(id);
        redisTemplate.delete("article::"+id);
        if(articleMapper.get(id).getTop()==true)
        {
            redisTemplate.delete("top");
        }
    }

    @Override
    public void update(ArticleWithBLOBs article) {
        articleMapper.updateByPrimaryKeySelective(article);     //若为更改则不更新时间
        zSet.add("visits","article::"+article.getId(),0);
        if(article.getTop()==true)
        {
            redisTemplate.delete("top");   //更新首页展示
        }
        writeOut();
        readIn();
    }

    private void setVisit(List<? extends Article> articles)
    {
        for (int i=0;i<articles.size();i++) {
            Article article=articles.get(i);
            if(redisTemplate.hasKey("article::"+article.getId()))
            {
                article.setVisit(getVisit(zSet.score("visits","article::"+article.getId())));
            }
        }
    }

    private void setCount(Article article)
    {
        article.setThumbsCount(thumbsMapper.countOf(article.getId()));
    }

    private void setThumb(Article item,String address)
    {
        item.setThumb(thumbsMapper.isThumb(item.getId(),address));
    }

    private void setTag(Article article)
    {
        article.setTag(tagService.get(article.getTid()));
    }

    private void setVisit(Article article)
    {
        Double visits =zSet.score("visits", "article::" + article.getId());
        if(visits!=null)
        {
            article.setVisit(visits.longValue());
        }
    }

    private void setCommentSize(Article article)
    {
        article.setCommentSize(commentService.countOfComment(article.getId()));
    }

    @Override
    public List<Article> listByTid(Integer tid,Boolean published) {
        List<Article> articles = articleMapper.listByTid(tid, published);
        for (Article article : articles) {
            setVisit(article);
            setCommentSize(article);
            setTag(article);
        }
        return articles;
    }

    @Override
    public ArticleWithBLOBs findById(Integer id,String address)
    {
        ArticleWithBLOBs article = articleMapper.findById(id);
        setThumb(article,address);
        setVisit(article);
        setCommentSize(article);
        setTag(article);
        return article;
    }

    @Override
    public List<ArticleWithBLOBs> findByTid(Integer tid,String address,Boolean published) {
        List<ArticleWithBLOBs> all = articleMapper.findByTid(tid,published);
        for (ArticleWithBLOBs item : all) {
            setThumb(item,address);
            setVisit(item);
            setCommentSize(item);
            setTag(item);
        }
        return all;       //前台只向用户展示已经出版的
    }

    @Override
    public List<Article> listAll(Boolean published) {
        List<Article> articles = articleMapper.listAll(published);
        for (Article article : articles) {
            setCommentSize(article);
            setVisit(article);
            setTag(article);
        }
        return articles;
    }

    @Override
    public List<TimeLine> timeLine() {
        return articleMapper.findTimeLines();
    }




    @Override
    public List<ArticleWithBLOBs> findAll(String address) {
        List<ArticleWithBLOBs> all = articleMapper.findAll();
        for (ArticleWithBLOBs item : all) {
            setThumb(item,address);
            setVisit(item);
            setCommentSize(item);
            setTag(item);
        }
        return all;       //前台只向用户展示已经出版的
    }


    @Override
    public List<ArticleWithBLOBs> findArticleLike(String s,String address) {
        List<ArticleWithBLOBs> all = articleMapper.findArticleLike("%"+s+"%");
        for (ArticleWithBLOBs item : all) {
            setThumb(item,address);
            setVisit(item);
            setCommentSize(item);
            setTag(item);
        }
        return all;
    }

    @Override
    public List<Article> listArticleLike(String s,Boolean published) {
        List<Article> articles = articleMapper.listArticleLike("%" + s + "%", published);
        for (Article article : articles) {
            setTag(article);
            setCommentSize(article);
            setVisit(article);
        }
        return articles;
    }

    @Override
    public List<Article> findLastestArticle() {
        List<Article> lastestArticle=null;
        if(redisTemplate.hasKey("LastArticle")==false)            //缓存未命中
        {
            LOG.info("最近更新文章缓存未命中");
            lastestArticle = articleMapper.findLastestArticle();
            redisTemplate.opsForValue().set("LastArticle",lastestArticle);
        }
        lastestArticle= (List<Article>) redisTemplate.opsForValue().get("LastArticle");
        for (Article article : lastestArticle) {
            setVisit(article);
            setCommentSize(article);
            setTag(article);
        }
        return lastestArticle;
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

    //访问量最大的三篇文章
    @Override
    public List<Article> foreArticle() {
        List<Article> articles=new LinkedList<>();
        if(redisTemplate.hasKey("visits")==false)   //缓存未命中,只能去数据库查找三次
        {
            LOG.info("缓存未命中,将从数据库中查找三次");
            readIn();
        }                                        //缓存命中,按照缓存里的数据去进行数据拼接
        Set<ZSetOperations.TypedTuple<Object>> visits = zSet.reverseRangeWithScores("visits",0,2);
        for (ZSetOperations.TypedTuple<Object> visit : visits) {
                Article article = articleMapper.get(getId(visit.getValue()));
                article.setVisit(getVisit(visit.getScore()));
                setCommentSize(article);
                articles.add(article);
            }
        return articles;
    }


    @Override
    public void incr(ArticleWithBLOBs article) {
        if(zSet.score("visits","article::"+article.getId())==null)
        {
            zSet.add("visits","article::"+article.getId(),article.getVisit());
        }
        zSet.incrementScore("visits","article::"+article.getId(),1);
        article.setVisit(getVisit(zSet.score("visits","article::"+article.getId())));   //文章本身访问量加一
    }

    @Override
    public List<ArticleWithBLOBs> topArts(String address,Boolean top) {
        if(redisTemplate.hasKey("top")==false)
        {
            List<ArticleWithBLOBs> all = articleMapper.findByTop(top);
            redisTemplate.opsForValue().set("top",all);
        }
        List<ArticleWithBLOBs> anw = (List<ArticleWithBLOBs>) redisTemplate.opsForValue().get("top");
        for (ArticleWithBLOBs item : anw) {
            setThumb(item,address);
            setVisit(item);
            setCommentSize(item);
            setTag(item);
            setCount(item);        //点赞数
        }
        return anw;
    }


    public void readIn()         //将数据库查询出的点击量最大的文章全部存入zset缓存中
    {
        List<Article> foreArticle = articleMapper.findForeArticle();
        for (Article article : foreArticle) {
            zSet.add("visits","article::"+article.getId(),article.getVisit());
        }
    }


    public void writeOut()  //将zset中初始化的数据全都存入数据库中
    {
        if(redisTemplate.hasKey("visits")){
            Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = zSet.rangeWithScores("visits",0,-1);
            for (ZSetOperations.TypedTuple<Object> objectTypedTuple : typedTupleSet) {
                Integer id=getId(objectTypedTuple.getValue());
                Long visit=getVisit(objectTypedTuple.getScore());
                articleMapper.updateArticleVisit(visit,id);
            }
            redisTemplate.delete("visits");
        }
        redisTemplate.delete("LastArticle");
        redisTemplate.delete("top");        //删除首页推送的缓存
    }

    //排行榜相关业务
    @PostConstruct        //启动后初始化会执行一次
    @Scheduled(cron = "0 0 1 * * ?")  //每天的凌晨一点执行一次
    public void initCache(){
        writeOut();
        readIn();
    }

}
