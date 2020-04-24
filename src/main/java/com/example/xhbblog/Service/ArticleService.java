package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.TimeLine;

import java.util.List;

public interface ArticleService {
    public void add(ArticleWithBLOBs article);
    public void remove(Integer id);
    public void update(ArticleWithBLOBs article);
    public List<Article> listByTid(Integer tid,Boolean published);   //用于后台
    public ArticleWithBLOBs findById(Integer id);
    public List<ArticleWithBLOBs> findByTid(Integer tid);
    public List<ArticleWithBLOBs> findAll();
    public List<ArticleWithBLOBs> findByVisit();
    List<ArticleWithBLOBs> findArticleLike(String s);
    List<Article> listArticleLike(String s,Boolean published); //用于后台
    List<ArticleWithBLOBs> foreArticle();
    List<ArticleWithBLOBs> findLastestArticle();
    public List<Article> listAll(Boolean published);
    public List<TimeLine> timeLine();        //这里我将时间轴写到了文章的业务层里面
    List<ArticleWithBLOBs> getLastestArticle();
}
