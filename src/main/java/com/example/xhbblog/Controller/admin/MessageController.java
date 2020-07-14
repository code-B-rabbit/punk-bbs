package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.MessageService;
import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.pojo.Message;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @ModelAttribute("msgCnt")
    public Long  msgs(@SessionAttribute("uid")Integer uid)
    {
        return userService.msgCnt(uid); //用户所获得的消息个数
    }

    @ModelAttribute("count")
    public Integer count(){
        return messageService.count();
    }

    @RequestMapping("/messageList")
    public String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model)
    {
        PageHelper.offsetPage(start,count);
        List<Message> list = messageService.list();
        model.addAttribute("page",new PageInfo<Message>(list));
        return "admin/messageList";
    }

    @RequestMapping("/deleteMessage")
    public String delete(Integer id)
    {
        messageService.delete(id);
        return "redirect:/admin/messageList";
    }

}
