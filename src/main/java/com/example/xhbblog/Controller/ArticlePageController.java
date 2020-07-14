package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.ArticleService;
import com.example.xhbblog.Service.CommentService;
import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.utils.IpUtil;
import com.example.xhbblog.utils.PageInfoUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        ArticleWithBLOBs article=articleService.findById(id, IpUtil.getIpAddr(request));
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
    public String comments(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "count", defaultValue = "5") Integer count, Model model, Integer aid)
    {
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
     * @param model
     * @param aid
     * @param comment
     * @param session
     * @return
     */
    @PostMapping("/Addcomment")
    public String comments(Model model, Integer aid, Comment comment, HttpSession session) throws IOException {
        User user= (User) session.getAttribute("user");
        comment.setUid(user.getId());             //设置评论的用户ID
        comment.setCreateTime(new Date());
        commentService.add(comment);
        commentService.sendComment(comment,user);   //用于后台消息推送
        return "redirect:/comments?aid="+aid;
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
        User user=userService.uid("匿名用户");
        comment.setUid(user.getId());
        comment.setCreateTime(new Date());
        commentService.add(comment);
        commentService.sendComment(comment,user);   //用于后台消息推送(这里匿名评论不允许回复)
        return "redirect:/comments?aid="+aid;
    }
}