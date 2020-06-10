package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.ArticleService;
import com.example.xhbblog.Service.FriendLyLinkService;
import com.example.xhbblog.Service.MessageService;
import com.example.xhbblog.pojo.FriendlyLink;
import com.example.xhbblog.pojo.Message;
import com.example.xhbblog.pojo.TimeLine;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class OtherController {

    @Autowired
    private ArticleService articleService;


    @Autowired
    private MessageService messageService;

    @Autowired
    private FriendLyLinkService friendLyLinkService;

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

    @GetMapping("/aboutMe")
    public String aboutMe(Model model)         //主页访问
    {
        model.addAttribute("articles", articleService.foreArticle());
        model.addAttribute("lastestArticles", articleService.findLastestArticle());
        return "home";
    }

    @PostMapping("/addFriendLyLink")
    public @ResponseBody
    String addLink(@RequestBody FriendlyLink friendlyLink)
    {
        friendLyLinkService.add(friendlyLink);
        return "您的请求已经发出,请等待站主进行紧张地审核";
    }
}
