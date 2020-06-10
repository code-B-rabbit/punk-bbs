package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.utils.MD5Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/adminLogin")       //后台登录页面路由
    public String login(Model model,@RequestParam(value = "message",defaultValue = "") String message)
    {
        model.addAttribute("message",message);
        return "admin/login";
    }

    @RequestMapping("/")
    public String index()
    {
        return "redirect:/admin/articleList";
    }


    @RequestMapping("/checkUser")
    public String checkUser(HttpSession session, User user, Model m) throws UnsupportedEncodingException {
        String s=user.getPassword();          //保存用户所输入的未加密密码
        User u=userService.check(user);
        if(u==null)
        {
            return "redirect:/admin/adminLogin?message="+ URLEncoder.encode("用户名或密码错误","utf8");
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

    @RequestMapping("/userList")
    public String list(@RequestParam(name = "start",defaultValue = "0") Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model)
    {
        PageHelper.offsetPage(start,count);
        List<User> list = userService.list();
        model.addAttribute("page",new PageInfo<User>(list));
        return "admin/userList";
    }

    @RequestMapping("/deleteUser")
    public String delete(Integer id)
    {
        userService.delete(id);
        return "redirect:/admin/userList";
    }

    @RequestMapping("/commentsByUser")
    public String comments(Integer id)
    {
        return "redirect:/admin/listByUid?uid="+id;
    }
}
