package com.example.xhbblog.controller.admin;

import com.example.xhbblog.service.CommentService;
import com.example.xhbblog.service.UserService;
import com.example.xhbblog.pojo.Comment;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminCommentController {   //后台评论只有查找和删除逻辑,添加在前台进行完成

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @ModelAttribute("msgCnt")
    public Long  msgs(@SessionAttribute("uid")Integer uid)
    {
        return userService.msgCnt(uid); //用户所获得的消息个数
    }


    @RequestMapping("/commentListOfAll")
    public String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "10")Integer count, Model model)
    {
        PageHelper.offsetPage(start,count);
        List<Comment> comments=commentService.list();
        model.addAttribute("page",new PageInfo<Comment>(comments));
        model.addAttribute("count",commentService.count());
        return "admin/commentList";
    }

    @RequestMapping("/listAnoComments")
    public String listByUid(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "10")Integer count,Model model)
    {
        PageHelper.offsetPage(start,count);
        List<Comment> comments=commentService.listAnonymousComment();
        model.addAttribute("page",new PageInfo<Comment>(comments));
        model.addAttribute("allAnonymous",true);   //说明是所有的匿名评论
        return "admin/commentList";
    }
}
