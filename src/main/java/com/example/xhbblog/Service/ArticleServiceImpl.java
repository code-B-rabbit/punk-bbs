package com.example.xhbblog.Service;

import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.TimeLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(ArticleWithBLOBs article) {
        articleMapper.insert(article);
    }

    @Override
    public void remove(Integer id) {
        articleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(ArticleWithBLOBs article) {
        articleMapper.updateByPrimaryKeySelective(article);     //若为更改则不更新时间
    }

    @Override
    public List<Article> listByTid(Integer tid,Boolean published) {
        return articleMapper.listByTid(tid,published);
    }

    @Override
    public ArticleWithBLOBs findById(Integer id) {
        return articleMapper.findById(id);
    }

    @Override
    public List<ArticleWithBLOBs> findByTid(Integer tid) {
        return articleMapper.findByTid(tid);
    }

    @Override
    public List<Article> listAll(Boolean published) {
        return articleMapper.listAll(published);     //用于后台预览全部
    }

    @Override
    public List<TimeLine> timeLine() {
        return articleMapper.findTimeLines();
    }

    @Override
    public List<ArticleWithBLOBs> getLastestArticle() {
        return articleMapper.getLastestArticle();
    }

    @Override
    public List<ArticleWithBLOBs> findAll() {
        return articleMapper.findAll();       //前台只向用户展示已经出版的
    }

    @Override
    public List<ArticleWithBLOBs> findByVisit() {
        return articleMapper.findByVisit();
    }

    @Override
    public List<ArticleWithBLOBs> findArticleLike(String s) {
        return articleMapper.findArticleLike("%"+s+"%");     //模糊查询具体逻辑在业务层完成
    }

    @Override
    public List<Article> listArticleLike(String s,Boolean published) {
        return articleMapper.listArticleLike("%"+s+"%",published);
    }

    @Override
    public List<ArticleWithBLOBs> foreArticle() {
        return articleMapper.findForeArticle();
    }

    @Override
    public List<ArticleWithBLOBs> findLastestArticle() {
        return articleMapper.findLastestArticle();
    }

}
