package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.EmailService;
import com.example.xhbblog.Service.FriendLyLinkService;
import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.pojo.FriendlyLink;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class FriendlyLinkController {          //这里提供改和同意以及不同意方法以及分页查询

    @Autowired
    private FriendLyLinkService friendLyLinkService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @ModelAttribute("msgCnt")
    public Long  msgs(@SessionAttribute("uid")Integer uid)
    {
        return userService.msgCnt(uid); //用户所获得的消息个数
    }


    @RequestMapping("/flList")
    public String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model,@RequestParam(name = "agree",defaultValue = "false")boolean agree)
    {
        PageHelper.offsetPage(start,count);
        List<FriendlyLink> list = friendLyLinkService.ListOf(agree);
        model.addAttribute("page",new PageInfo<FriendlyLink>(list));
        model.addAttribute("limit","agree="+agree);
        model.addAttribute("agree",agree);
        return "admin/flList";
    }


    @RequestMapping("/addFl")
    public String add(FriendlyLink fl)
    {
        friendLyLinkService.add(fl);
        return "redirect:/admin/flList?agree="+true;
    }

    @RequestMapping("/deleteFl")
    public String delete(Integer id,boolean agree)
    {
        friendLyLinkService.delete(id);
        return "redirect:/admin/flList?agree="+agree;
    }

    @RequestMapping("/checkAgree")     //审核查询友链并发送邮件
    public String agree(@RequestParam(name = "start",defaultValue = "0")Integer start,@RequestParam(name = "id",defaultValue = "0")Integer id)  //存储start
    {
        FriendlyLink friendlyLink = friendLyLinkService.get(id);
        friendlyLink.setAllowed(true);
        friendLyLinkService.update(friendlyLink);
        emailService.sendEmail("友链申请成功通知",friendlyLink.getEmail(),"您的网站" +friendlyLink.getLink()+
                "在xuhaobo.site的友链申请已成功"+"如非本人操作请忽略");
        return "redirect:/admin/flList?start="+start+"&agree=false";    //一定在未同意的界面进行同意的操作
    }


}
