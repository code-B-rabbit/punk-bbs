package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.ArticleService;
import com.example.xhbblog.Service.CommentService;
import com.example.xhbblog.Service.TagService;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WxAppController {

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @RequestMapping("/tags")
    public List<Tag> findAll()
    {
       return tagService.list();
    }

    @RequestMapping("/listByTag/{tid}")
    public List<Article> listById(@PathVariable("tid") Integer tid)
    {
        return articleService.listByTid(tid,true);
    }

    @RequestMapping("/article/{id}")
    public Article get(@PathVariable("id") Integer id)
    {
        return articleService.findById(id);
    }

    @RequestMapping("/comments/{aid}")
    public List<Comment> list(@PathVariable("aid") Integer aid)
    {
        return commentService.listByAid(aid);
    }

}
