package com.example.xhbblog.manager;

import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.mapper.CommentMapper;
import com.example.xhbblog.mapper.ThumbsMapper;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.utils.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@Transactional
public class RedisArticleManager {
    private Logger LOG= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ThumbsMapper thumbsMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ZSetOperations zSet;

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

    public void deleteKey(String key){
        redisTemplate.delete(key);
    }


    /**
     * 后台修改文章
     * @param article
     */
    public void updateArt(Article article){
        zSet.add(RedisKey.ART_VISITS_ZSET, RedisKey.ART_VISIT+article.getId(),0);  //修改该文章的访问量
        this.writeOut();
        this.readIn();
    }

    /**
     * 最新更新的文章
     * @return
     */
    public List<Article> lastestArticle(){
        List<Article> lastestArticle=null;
        if(redisTemplate.hasKey(RedisKey.LAST_ARTICLE)==false)            //缓存未命中
        {
            LOG.info("最近更新文章缓存未命中");
            lastestArticle = articleMapper.findLastestArticle();
            redisTemplate.opsForValue().set(RedisKey.LAST_ARTICLE,lastestArticle);
        }
        lastestArticle= (List<Article>) redisTemplate.opsForValue().get(RedisKey.LAST_ARTICLE);
        return lastestArticle;
    }

    /**
     * 首页浏览最多的文章
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> foreArticle(){
        if(redisTemplate.hasKey(RedisKey.ART_VISITS_ZSET)==false)   //缓存未命中,只能去数据库查找三次
        {
            LOG.info("缓存未命中,将从数据库中查找三次");
            readIn();
        }                                        //缓存命中,按照缓存里的数据去进行数据拼接
        return zSet.reverseRangeWithScores(RedisKey.ART_VISITS_ZSET,0,2);
    }

    /**
     * 浏览数加一的业务
     * 若不存在,则加一后写入到缓存中
     * @param article
     */
    public void incr(ArticleWithBLOBs article) {
        if(zSet.score(RedisKey.ART_VISITS_ZSET, RedisKey.ART_VISIT+article.getId())==null)
        {
            LOG.info("文章{}访问量,开始写入",article.getTitle());
            zSet.add(RedisKey.ART_VISITS_ZSET, RedisKey.ART_VISIT+article.getId(),article.getVisit());
        }
        LOG.info("文章{}访问量加一",article.getTitle());
        zSet.incrementScore(RedisKey.ART_VISITS_ZSET, RedisKey.ART_VISIT+article.getId(),1);
        article.setVisit(getVisit(zSet.score(RedisKey.ART_VISITS_ZSET, RedisKey.ART_VISIT+article.getId())));   //文章本身访问量加一
    }


    /**
     * 从缓存中取出浏览量并设置在文章字段上
     * @param article
     */
    public void setVisit(Article article){
        Double visits =zSet.score(RedisKey.ART_VISITS_ZSET, RedisKey.ART_VISIT + article.getId());
        if(visits!=null)
        {
            article.setVisit(visits.longValue());
        }else{
            LOG.info("文章{}访问量未改变",article.getTitle());
        }
    }


    /**
     * 置顶文章
     * @param top
     * @param uid
     * @return
     */
    public List<ArticleWithBLOBs> topArts(Boolean top,Integer uid)
    {
        List<ArticleWithBLOBs> anw=null;
        if(uid!=null){
            anw = articleMapper.findByTop(true,uid);
        }else{
            if(redisTemplate.hasKey(RedisKey.ART_TOP)==false)
            {
                List<ArticleWithBLOBs> all = articleMapper.findByTop(top,uid);
                redisTemplate.opsForValue().set(RedisKey.ART_TOP,all);
            }
            anw = (List<ArticleWithBLOBs>) redisTemplate.opsForValue().get(RedisKey.ART_TOP);
        }
        return anw;
    }

    /**
     * 删除置顶
     */
    public void delTop(){
        redisTemplate.delete(RedisKey.ART_TOP);
    }

    public void deleteArticle(Integer id, Integer uid) {
        if(articleMapper.get(id).getTop()==true)
        {
            redisTemplate.delete(RedisKey.ART_TOP);
        }
        thumbsMapper.deleteThumbByAid(id);            //级联删除
        for (Comment comment : commentMapper.listByAid(id)) {
            commentMapper.deleteByPrimaryKey(comment.getId());
        }
        redisTemplate.delete(RedisKey.THUMBS_SET+id);     //删除对应的点赞缓存
        redisTemplate.delete(RedisKey.THUMB_EXIST+id);  //设置点赞缓存不存在,防止留下缓存项引起脏数据
        redisTemplate.delete(RedisKey.ART_VISITS_ZSET);   //更新访问最多
        redisTemplate.delete(RedisKey.LAST_ARTICLE);  //更新最近访问
        redisTemplate.delete(RedisKey.COUNTOF_ARTICLE+id);
        redisTemplate.delete(RedisKey.COUNTOF_COMMENT+id);
        redisTemplate.delete(RedisKey.LAST_COMMENT);
        if(uid!=null){
            articleMapper.deleteArticle(id,uid);   //用于校验安全性
        }else{
            articleMapper.deleteByPrimaryKey(id);
        }
    }



    public void readIn()         //将数据库查询出的点击量最大的文章全部存入zset缓存中
    {
        List<Article> foreArticle = articleMapper.findForeArticle();
        for (Article article : foreArticle) {
            zSet.add(RedisKey.ART_VISITS_ZSET, RedisKey.ART_VISIT+article.getId(),article.getVisit());
        }
    }


    public void writeOut()  //将zset中初始化的数据全都存入数据库中
    {
        if(redisTemplate.hasKey(RedisKey.ART_VISITS_ZSET)){
            Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = zSet.rangeWithScores(RedisKey.ART_VISITS_ZSET,0,-1);
            for (ZSetOperations.TypedTuple<Object> objectTypedTuple : typedTupleSet) {
                Integer id=getId(objectTypedTuple.getValue());
                Long visit=getVisit(objectTypedTuple.getScore());
                articleMapper.updateArticleVisit(visit,id);
            }
            redisTemplate.delete(RedisKey.ART_VISITS_ZSET);
        }
        redisTemplate.delete(RedisKey.LAST_ARTICLE);
        redisTemplate.delete(RedisKey.ART_TOP);        //删除首页推送的缓存
    }


}
