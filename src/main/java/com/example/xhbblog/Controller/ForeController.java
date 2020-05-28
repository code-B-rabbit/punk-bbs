package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.*;
import com.example.xhbblog.pojo.*;
import com.example.xhbblog.util.IpUtil;
import com.example.xhbblog.util.PageInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    public void foreSelect(Model model)   //首页查询
    {
        model.addAttribute("lastestArticles", articleService.findLastestArticle());
        model.addAttribute("tags", tagService.list());
        model.addAttribute("visitMoreArticles", articleService.foreArticle());
    }

    @GetMapping("/aboutMe")
    public String aboutMe(Model model)         //主页访问
    {
        model.addAttribute("articles", articleService.foreArticle());
        model.addAttribute("lastestArticles", articleService.findLastestArticle());
        return "home";
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
        foreSelect(model);
        model.addAttribute("title","XHB's Blog");   //用于显示标题
        return "blog";
    }

    @GetMapping("/articlesByTag")
    public String articles(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "6") Integer count, Model model, Integer tid
    ,HttpServletRequest request) {
        PageHelper.offsetPage(start, count);
        List<ArticleWithBLOBs> all = articleService.findByTid(tid,IpUtil.getIpAddr(request),true);
        model.addAttribute("page", new PageInfo<ArticleWithBLOBs>(all));
        foreSelect(model);
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
        foreSelect(model);
        model.addAttribute("limit","search="+search);
        model.addAttribute("title",search+" 的搜索结果");
        return "blog";
    }

    @PostMapping("/addFriendLyLink")
    public @ResponseBody String addLink(@RequestBody FriendlyLink friendlyLink)
    {
        friendLyLinkService.add(friendlyLink);
        return "您的请求已经发出,请等待站主进行紧张地审核";
    }

    @GetMapping("/article")
    public String article(Integer id,Model model,HttpServletRequest request)
    {
        ArticleWithBLOBs article=articleService.findById(id,IpUtil.getIpAddr(request));
        articleService.incr(article);
        model.addAttribute("art",article);   //所查找到的文章
        return "post";
    }

    //这里pageHelper分页失效了,一时半会没找到解决方案,就只能这么做了
    public PageInfo<Comment> getPageInfo(Integer total,List<Comment> comments,Integer start,Integer count)       //这里pagehelper分页失效了就只能这么做了
    {
        PageInfo<Comment> pageInfo=new PageInfo<Comment>(comments);
        pageInfo.setTotal(total);
        pageInfo= PageInfoUtil.get(pageInfo,start,count);
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

    @PostMapping("/Addcomment")
    public String comments(Model model,Integer aid,Comment comment,HttpSession session)
    {
        User user= (User) session.getAttribute("user");
        comment.setUid(user.getId());             //设置评论的用户ID
        comment.setCreateTime(new Date());
        commentService.add(comment);
        return "redirect:/comments?aid="+aid;
    }

    @GetMapping("timeLine")
    public String timeLine(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "5") Integer count, Model model)
    {
        PageHelper.offsetPage(start,count);
        List<TimeLine> timeLines = articleService.timeLine();
        model.addAttribute("page",new PageInfo<TimeLine>(timeLines));
        return "timestamp";
    }


    @RequestMapping("/messages")
    public String messages(Model model)
    {
        return "messages";         //只返回分页后的列表
    }

    @GetMapping("/messageList")
    public String messages(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "10") Integer count, Model model)
    {
        model.addAttribute("total",messageService.count());    //留言总数显示
        PageHelper.offsetPage(start, count);
        List<Message> list = messageService.list();
        PageInfo<Message> pageInfo=new PageInfo<Message>(list);
        model.addAttribute("page",pageInfo);
        return "card::card";         //只返回分页后的列表
    }


    @PostMapping("/Addmessage")
    public String messages(Model model, Integer aid, Message message)
    {
        message.setCreateTime(new Date());
        messageService.add(message);
        return "redirect:/messageList";
    }

    @GetMapping("/searchAnswer")
    public String search(Model model)
    {
        return "searchAnswer";
    }
}