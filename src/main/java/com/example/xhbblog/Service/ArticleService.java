package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.TimeLine;

import java.util.List;

public interface ArticleService {
    public void add(Article article);
    public void remove(Integer id);
    public void update(Article article);
    public List<Article> listByTid(Integer tid,Boolean published);   //用于后台
    public Article findById(Integer id);
    public List<Article> findByTid(Integer tid);
    public List<Article> findAll();
    public List<Article> findByVisit();
    List<Article> findArticleLike(String s);
    List<Article> listArticleLike(String s,Boolean published); //用于后台
    List<Article> foreArticle();
    List<Article> findLastestArticle();
    public List<Article> listAll(Boolean published);
    public List<TimeLine> timeLine();        //这里我将时间轴写到了文章的业务层里面
    List<Article> getLastestArticle();
}
