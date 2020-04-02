package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/adminLogin")       //后台登录页面路由
    public String login()
    {
        return "admin/login";
    }


    @RequestMapping("/checkUser")
    public String checkUser(HttpSession session, User user, Model m, RedirectAttributes attributes)
    {
        String s=user.getPassword();          //保存用户所输入的未加密密码
        User u=userService.check(user);
        if(u==null)
        {
            attributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin/adminLogin";
        }else
        {
            u.setPassword(s);    //将用户所输入的未加密密码存回去
            session.setAttribute("user",u);
            return "redirect:/admin/articleList";
        }
    }


    @RequestMapping("/changePassword")
    public String changePassword(User user,HttpSession session)
    {
        user.setPassword(MD5Utils.code(user.getPassword()));     //密码加密
        userService.update(user);
        session.removeAttribute("user");
        return "redirect:/admin/adminLogin";
    }

    @RequestMapping("/exit")           //退出登录
    public String removeUser(HttpSession session)
    {
        session.removeAttribute("user");
        return "redirect:/admin/adminLogin";
    }

    @RequestMapping("/user")
    public String user()
    {
        return "admin/user";
    }

    @RequestMapping("/changeAccount")
    public String update(@Valid User user, RedirectAttributes attributes, BindingResult result,HttpSession session)      //同时进行表单校验
    {
        String password=user.getPassword();            //先保存这个密码的值
        if(result.hasErrors())
        {
            attributes.addFlashAttribute("message",result.getFieldError().getDefaultMessage());
        }else
        {
            user.setPassword(MD5Utils.code(user.getPassword()));         //密码加密
            userService.update(user);
            session.removeAttribute("user");
            attributes.addFlashAttribute("message","修改成功,请重新登录");
        }
        return "redirect:/admin/login";
    }
}
