package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.*;
import com.example.xhbblog.pojo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class ForeController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private FriendLyLinkService friendLyLinkService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageService messageService;


    @RequestMapping("/")
    public String fore(Model model)         //主页访问
    {
        model.addAttribute("articles", articleService.foreArticle());
        model.addAttribute("lastestArticles", articleService.getLastestArticle());
        model.addAttribute("fls", friendLyLinkService.ListOf(true));
        return "index";
    }

    @RequestMapping("/articles")
    public String articles(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "4") Integer count, Model model) {
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findAll();
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        model.addAttribute("lastestArticles", articleService.findLastestArticle());
        model.addAttribute("tags", tagService.list());
        model.addAttribute("fls", friendLyLinkService.ListOf(true));   //友链
        model.addAttribute("title","博客列表");   //用于显示标题
        return "blog";
    }

    @RequestMapping("/articlesByTag")
    public String articles(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "4") Integer count, Model model, Integer tid) {
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findByTid(tid);
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        model.addAttribute("lastestArticles", articleService.findLastestArticle());
        model.addAttribute("tags", tagService.list());
        model.addAttribute("fls", friendLyLinkService.ListOf(true));   //友链
        model.addAttribute("tid",tid);
        model.addAttribute("limit","tid="+tid);
        model.addAttribute("title","标签:"+tagService.get(tid).getName());
        return "blog";
    }

    @RequestMapping("/blogsearch")
    public String articles(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "4") Integer count, Model model, String search) {
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findArticleLike(search);
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        model.addAttribute("lastestArticles", articleService.findLastestArticle());
        model.addAttribute("tags", tagService.list());
        model.addAttribute("fls", friendLyLinkService.ListOf(true));   //友链
        model.addAttribute("limit","search="+search);
        model.addAttribute("title",search+" 的搜索结果");
        return "blog";
    }

    @RequestMapping("/addFriendLyLink")
    public @ResponseBody String addLink(@RequestBody FriendlyLink friendlyLink)
    {
        friendLyLinkService.add(friendlyLink);
        return "您的请求已经发出,请等待站主进行紧张地审核";
    }

    @RequestMapping("/article")
    public String article(Integer id,Model model)
    {
        ArticleWithBLOBs article=articleService.findById(id);
        //System.out.println(article);
        if(article.getVisit()==null)
        {
            article.setVisit(new Long(1));
        }else
        {
            article.setVisit(article.getVisit()+1);           //访问量加1
        }
        model.addAttribute("art",articleService.findById(id));   //所查找到的文章
        model.addAttribute("lastestArticles", articleService.findLastestArticle());
        //model.addAttribute("comments",commentService.findByAid(id));
        model.addAttribute("tags", tagService.list());
        model.addAttribute("fls", friendLyLinkService.ListOf(true));   //友链
        articleService.update(article);
        return "post";
    }

    //这里pageHelper分页失效了,一时半会没找到解决方案,就只能这么做了
    public PageInfo<Comment> getPageInfo(Integer total,List<Comment> comments,Integer start,Integer count)       //这里pagehelper分页失效了就只能这么做了
    {
        PageInfo<Comment> pageInfo=new PageInfo<Comment>(comments);
        pageInfo.setTotal(total);
        pageInfo.setHasNextPage((start+count)<pageInfo.getTotal());
        pageInfo.setHasPreviousPage(start-count>=0);
        pageInfo.setPageNum(start/count+1);
        pageInfo.setPageSize(count);
        return  pageInfo;
    }

    @RequestMapping("/comments")
    public String comments(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "5") Integer count, Model model,Integer aid)
    {
        Integer total=commentService.countOfArticle(aid);
        Integer commentSize=commentService.countOfComment(aid);
        model.addAttribute("aid",aid);
        model.addAttribute("total",commentSize);    //评论总数显示;
        List<Comment> byAid = commentService.findByAid(aid,start,count);
        PageInfo<Comment> pageInfo = getPageInfo(total, byAid, start, count);
        model.addAttribute("page",pageInfo);
        return "comment::comments";         //只返回分页后的列表
    }

    @RequestMapping("/Addcomment")
    public String comments(Model model,Integer aid,Comment comment)
    {
        comment.setCreateTime(new Date());
        commentService.add(comment);
        return "redirect:/comments?aid="+aid;
    }

    @RequestMapping("timeLine")
    public String timeLine(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "5") Integer count, Model model)
    {
        model.addAttribute("fls", friendLyLinkService.ListOf(true));
        PageHelper.offsetPage(start,count);
        List<TimeLine> timeLines = articleService.timeLine();
        model.addAttribute("page",new PageInfo<TimeLine>(timeLines));
        return "timestamp";
    }


    @RequestMapping("/messages")
    public String messages(Model model)
    {
        model.addAttribute("fls", friendLyLinkService.ListOf(true));
        return "messages";         //只返回分页后的列表
    }

    @RequestMapping("/messageList")
    public String messages(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "10") Integer count, Model model)
    {
        model.addAttribute("total",messageService.count());    //留言总数显示
        PageHelper.offsetPage(start, count);
        List<Message> list = messageService.list();
        PageInfo<Message> pageInfo=new PageInfo<Message>(list);
        model.addAttribute("page",pageInfo);
        return "card::card";         //只返回分页后的列表
    }


    @RequestMapping("/Addmessage")
    public String messages(Model model, Integer aid, Message message)
    {
        message.setCreateTime(new Date());
        messageService.add(message);
        return "redirect:/messageList";
    }
}