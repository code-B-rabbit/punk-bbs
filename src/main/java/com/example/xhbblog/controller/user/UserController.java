package com.example.xhbblog.controller.user;

import com.example.xhbblog.service.UserService;
import com.example.xhbblog.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台用户管理
 */
@Controller
@RequestMapping({"/userAdmin","/admin"})
public class UserController {

    @Autowired
    private UserService userService;

    @ModelAttribute("msgCnt")
    public Long  msgs(HttpSession session)
    {
        Integer uid= (Integer) session.getAttribute("uid");
        if(uid!=null){
           return userService.msgCnt(uid); //用户所获得的消息个数
       }
       return  null;
    }

    /**
     * 退出
     * 实际上这个跳转请求此时已经被拦截了
     * @param session
     * @return
     */
    @RequestMapping("/exit")           //退出登录
    public String removeUser(HttpSession session)
    {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/userAdmin/adminLogin";
    }


    @RequestMapping("/user")
    public String user()
    {
        return "admin/user";
    }

    @PostMapping("/changeAccount")
    public @ResponseBody Map update(User user, RedirectAttributes attributes, BindingResult result,HttpSession session)      //同时进行表单校验
    {
        Map<String, String> response = new HashMap<>();
        if(result.hasErrors())
        {
            attributes.addFlashAttribute("message",result.getFieldError().getDefaultMessage());
            response.put("msg","系统错误");
        }else
        {
            userService.update(user);
            session.setAttribute("user",user);
            response.put("msg","用户信息修改成功!!");
        }
        return response;
    }

    @RequestMapping("/commentsByUser")
    public String comments(Integer id)
    {
        return "redirect:listByUid?uid="+id;
    }

    /**
     * 异步请求推送信息
     * @return
     */
    @RequestMapping("/messages")
    public @ResponseBody Object getMessages(@SessionAttribute("uid")Integer uid)
    {
        Map<String,Object> anw=new HashMap<>();
        anw.put("data",userService.getMessages(uid));
        return anw;
    }

    /**
     * 异步请求将未设置已读的信息重新添加回去
     * @param uid
     * @return
     */
    @PostMapping("/messagesNotRead")
    public @ResponseBody void addMessages(@RequestParam(name = "messageList") String[] messageList,
                                           @SessionAttribute("uid")Integer uid) throws UnsupportedEncodingException {
        for(int i=0;i<messageList.length;i++){
            messageList[i]= URLDecoder.decode(messageList[i],"utf-8");
        }
        userService.addMessages(messageList,uid);
    }

    @GetMapping("/advice")
    public ModelAndView advice(){
        return new ModelAndView("/admin/advice");
    }

}
