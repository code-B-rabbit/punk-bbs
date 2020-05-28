package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.TimeLine;

import java.util.List;

public interface ArticleService {
    public void add(ArticleWithBLOBs article);
    public void remove(Integer id);
    public void update(ArticleWithBLOBs article);
    public ArticleWithBLOBs findById(Integer id,String address);
    public List<ArticleWithBLOBs> findByTid(Integer tid,String address,Boolean published);
    public List<ArticleWithBLOBs> findAll(String address);
    List<ArticleWithBLOBs> findArticleLike(String s, String address);
    public List<Article> listByTid(Integer tid, Boolean published);   //用于后台
    public List<Article> listAll(Boolean published);
    List<Article> listArticleLike(String s,Boolean published); //用于后台
    List<Article> foreArticle();
    List<Article> findLastestArticle();
    public List<TimeLine> timeLine();        //这里我将时间轴写到了文章的业务层里面
    public void incr(ArticleWithBLOBs article);
    public List<ArticleWithBLOBs> topArts(String address,Boolean top);  //首页的博客
}
