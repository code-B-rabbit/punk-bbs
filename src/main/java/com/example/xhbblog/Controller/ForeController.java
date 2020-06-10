package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.*;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.Tag;
import com.example.xhbblog.utils.IpUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ForeController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    @ModelAttribute("lastestArticles")
    public List<Article>  lastestArticles()
    {
        return articleService.findLastestArticle();
    }

    @ModelAttribute("visitMoreArticles")
    public List<Article>  foreArticle()
    {
        return articleService.foreArticle();
    }

    @ModelAttribute("tags")
    public List<Tag>  tag()
    {
        return tagService.list();
    }

    @ModelAttribute("lastComments")
    public List<Comment> comments()
    {
        return commentService.lastComment();
    }

    @GetMapping({"/","/articles"})
    public String articles(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "6") Integer count, Model model, HttpServletRequest request) {
        String ip=IpUtil.getIpAddr(request);
        if(start==0)
        {
            model.addAttribute("tops",articleService.topArts(ip,true));
        }
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findAll(ip);   //获取一步ip地址
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        model.addAttribute("title","XHB's Blog");   //用于显示标题
        return "blog";
    }

    @GetMapping("/articlesByTag")
    public String articles(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "6") Integer count, Model model, Integer tid
    ,HttpServletRequest request) {
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findByTid(tid,IpUtil.getIpAddr(request),true);
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        model.addAttribute("tid",tid);
        model.addAttribute("limit","tid="+tid);
        model.addAttribute("title","标签:"+tagService.get(tid).getName());
        return "blog";
    }

    @GetMapping("/blogsearch")
    public String articles(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "6") Integer count, Model model, String search
    ,HttpServletRequest request) {
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findArticleLike(search, IpUtil.getIpAddr(request));
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        model.addAttribute("limit","search="+search);
        model.addAttribute("title",search+" 的搜索结果");
        return "blog";
    }



    //这里pageHelper分页失效了,一时半会没找到解决方案,就只能这么做了


}