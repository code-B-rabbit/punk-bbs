package com.example.xhbblog.controller;

import com.example.xhbblog.service.ArticleService;
import com.example.xhbblog.service.CommentService;
import com.example.xhbblog.service.UserService;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.utils.IpUtil;
import com.example.xhbblog.utils.PageInfoUtil;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class ArticlePageController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    /**
     * 查看文章
     * @param id
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/article")
    public String article(Integer id, Model model, HttpServletRequest request)
    {
        Subject subject = SecurityUtils.getSubject();
        User user= (User) subject.getPrincipal();
        if(user==null){
            user = new User();
            user.setRole("user");
        }
        ArticleWithBLOBs article=articleService.findById(id, IpUtil.getIpAddr(request),user.getRole());
        articleService.incr(article);
        model.addAttribute("art",article);   //所查找到的文章
        return "post";
    }

    /**
     * 获得分页对象
     * @param total
     * @param comments
     * @param start
     * @param count
     * @return
     */
    public PageInfo<Comment> getPageInfo(Integer total, List<Comment> comments, Integer start, Integer count)       //这里pagehelper分页失效了就只能这么做了
    {
        PageInfo<Comment> pageInfo=new PageInfo<Comment>(comments);
        pageInfo.setTotal(total);
        pageInfo= PageInfoUtil.get(pageInfo,start,count);
        return  pageInfo;
    }

    /**
     * 查看某文章对应的评论
     * @param start
     * @param count
     * @param model
     * @param aid
     * @return
     */
    @RequestMapping("/comments")
    public String comments(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "5") Integer count, Model model, Integer aid,Boolean error)
    {
        if(error!=null) //出现登录超时错误
        {
            model.addAttribute("res","登录超时,请重新登录");
        }
        Integer total=commentService.countOfArticle(aid);
        Integer commentSize=commentService.countOfComment(aid);
        List<Comment> byAid = commentService.findByAid(aid,start,count);
        PageInfo<Comment> pageInfo = getPageInfo(total, byAid, start, count);
        model.addAttribute("page",pageInfo);
        model.addAttribute("aid",aid);
        model.addAttribute("total",commentSize);    //评论总数显示;
        return "comment::comments";         //只返回分页后的列表
    }

    /**
     * 添加评论
     * @param aid
     * @param comment
     * @return
     */
    @PostMapping("/Addcomment")
    @RequiresAuthentication
    public String comments(Integer aid, Comment comment) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        User user= (User) subject.getPrincipal();
        if(user==null){
            return "redirect:/comments?aid="+aid+"&error="+true;
        }else{
            comment.setUid(user.getId());
            comment.setUid(user.getId());             //设置评论的用户ID
            commentService.add(comment);
            commentService.sendComment(comment,user);   //用于后台消息推送
            return "redirect:/comments?aid="+aid;
        }
    }

    /**
     * 匿名评论
     * @param model
     * @param aid
     * @param comment
     * @param session
     * @return
     */
    @PostMapping("/AddcommentNoName")
    public String noNameAdd(Model model, Integer aid, Comment comment, HttpSession session) throws IOException {
        User user=userService.findByName("匿名游客");
        comment.setUid(user.getId());
        commentService.add(comment);
        commentService.sendComment(comment,user);   //用于后台消息推送(这里匿名评论不允许回复)
        return "redirect:/comments?aid="+aid;
    }
}
