package com.example.xhbblog.service;


import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.TimeLine;

import java.util.List;

/**
 * 博客文章的业务接口
 */
public interface ArticleService {

    public void add(ArticleWithBLOBs article);
    public void update(ArticleWithBLOBs article);
    public ArticleWithBLOBs get(Integer id);
    public List<ArticleWithBLOBs> listByTid(Integer tid, Boolean published,Integer rank,Integer uid);   //用于后台
    public List<ArticleWithBLOBs> listAll(Boolean published,Integer rank,Integer uid);
    public List<ArticleWithBLOBs> listArticleLike(String s,Boolean published,Integer rank,Integer uid); //用于后台

    public ArticleWithBLOBs findById(Integer id,String address,String role);
    public List<ArticleWithBLOBs> findByUid(Integer uid,String address,Boolean published);
    public List<ArticleWithBLOBs> findByTid(Integer tid,String address,Boolean published);
    public List<ArticleWithBLOBs> findAll(String address);
    public List<ArticleWithBLOBs> findArticleLike(String s, String address);
    public List<Article> foreArticle();
    public List<Article> findLastestArticle();

    public Article getTitle(Integer id);
    public List<TimeLine> timeLine();        //这里我将时间轴写到了文章的业务层里面
    public void incr(ArticleWithBLOBs article);
    public List<ArticleWithBLOBs> topArts(String address,Boolean top,Integer uid);  //首页的博客
    public void topArticle(Integer id);
    public void downArticle(Integer id);
    public Integer count();

    void deleteArticle(Integer id,Integer uid);
    public void initCache();

}
