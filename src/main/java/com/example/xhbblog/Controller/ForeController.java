package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.*;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.Tag;
import com.example.xhbblog.utils.IpUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 首页控制器
 */
@Controller
public class ForeController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    /**
     * 最新文章
     * @return
     */
    @ModelAttribute("lastestArticles")
    public List<Article>  lastestArticles()
    {
        return articleService.findLastestArticle();
    }

    /**
     * 访问最多
     * @return
     */
    @ModelAttribute("visitMoreArticles")
    public List<Article>  foreArticle()
    {
        return articleService.foreArticle();
    }

    /**
     * 全部标签
     * @return
     */
    @ModelAttribute("tags")
    public List<Tag>  tag()
    {
        return tagService.list();
    }

    /**
     * 最新评论
     * @return
     */
    @ModelAttribute("lastComments")
    public List<Comment> comments()
    {
        return commentService.lastComment();
    }

    /**
     * 首页
     * @param start
     * @param count
     * @param model
     * @param request
     * @return
     */
    @GetMapping({"/","/articles"})
    public String articles(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "6") Integer count, Model model, HttpServletRequest request) {
        String ip=IpUtil.getIpAddr(request);
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findAll(ip);   //获取一步ip地址
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        model.addAttribute("title","XHB's Blog");   //用于显示标题
        if(start==0)
        {
            model.addAttribute("tops",articleService.topArts(ip,true,null));
        }
        return "blog";
    }

    /**
     * 某标签下的全部文章
     * @param start
     * @param count
     * @param model
     * @param tid
     * @param request
     * @return
     */
    @GetMapping("/articlesByTag")
    public String articlesByTag(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "6") Integer count, Model model, Integer tid
    ,HttpServletRequest request) {
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findByTid(tid,IpUtil.getIpAddr(request),true);
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        model.addAttribute("tid",tid);
        model.addAttribute("limit","tid="+tid);
        model.addAttribute("title","标签:"+tagService.get(tid).getName());
        return "blog";
    }


    /**
     * 查询某个作者的全部文章
     * @param start
     * @param count
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/articlesByUser")
    public String articles(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "6") Integer count, Model model, Integer uid
            ,HttpServletRequest request) {
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findByUid(uid,IpUtil.getIpAddr(request),true);
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        model.addAttribute("uid",uid);
        model.addAttribute("limit","uid="+uid);
        model.addAttribute("title","作者:"+userService.get(uid).getName());
        return "blog";
    }




    /**
     * 搜索
     * @param start
     * @param count
     * @param model
     * @param search
     * @param request
     * @return
     */
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