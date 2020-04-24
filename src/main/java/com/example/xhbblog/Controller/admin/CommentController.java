package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.ArticleService;
import com.example.xhbblog.Service.CommentService;
import com.example.xhbblog.pojo.Comment;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CommentController {   //后台评论只有查找和删除逻辑,添加在前台进行完成

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/commentList")
    public String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "100")Integer count, Model model)
    {
        PageHelper.offsetPage(start,count);
        List<Comment> comments=commentService.list();
        model.addAttribute("page",new PageInfo<Comment>(comments));
        return "admin/commentList";
    }

    @RequestMapping("/deleteComment")
    public String delete(Integer id,Integer aid)
    {
        commentService.delete(id);
        return "redirect:/admin/listByAid?aid="+aid;
    }

    @RequestMapping("/listByAid")
    public String listByAid(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "10")Integer count,Integer aid,Model model)
    {
        model.addAttribute("limit","aid="+aid);
        model.addAttribute("article",articleService.findById(aid));
        PageHelper.offsetPage(start,count);
        List<Comment> comments=commentService.listByAid(aid);
        model.addAttribute("page",new PageInfo<Comment>(comments));
        return "admin/commentList";
    }

}
