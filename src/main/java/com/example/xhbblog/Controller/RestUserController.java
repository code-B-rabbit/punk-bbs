package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.annotation.AccessLimit;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class RestUserController {

    @Autowired
    private UserService userService;


    /**
     * 用户验证
     * 十分钟内只允许十次
     * @param user
     * @param session
     * @param request
     * @param response
     * @return
     */
    @AccessLimit(maxCount = 10,seconds = 600)
    @PostMapping("/check")
    public Map<String,Boolean> checkUser(User user, HttpSession session, HttpServletRequest request,HttpServletResponse response)
    {
        String password=user.getPassword();        //暂时存储一下旧的密码
        Map<String,Boolean> anw=new HashMap<>();
        User u=userService.check(user);
        if(u==null)
        {
            anw.put("message",false);
        }else
        {
            anw.put("message",true);
            anw.put("adm",u.getRole().equals("admin"));
            if(user.getChecked()!=null&&user.getChecked()==true)
            {
                CookieUtil.set(response,"name",user.getName(),true);
                CookieUtil.set(response,"password",password,true);
            }else{
                CookieUtil.remove(request,response,"name");
                CookieUtil.remove(request,response,"password");
            }
            u.setPassword(password);
            session.setAttribute("user",u);
            session.setAttribute("uid",u.getId());
            session.setAttribute("admin",u.getRole().equals("admin"));
        }
        return anw;
    }

    /**
     * 验证是否登录
     * @param session
     * @return
     */
    @PostMapping("/check/login")    //是否登录
    public Map<String,Boolean> check(HttpSession session)
    {
        Map<String,Boolean> anw=new HashMap<>();
        anw.put("has",session.getAttribute("user")!=null);
        return anw;
    }

    /**
     * 登录页路由
     * @return
     */
    @GetMapping("/login")
    public ModelAndView login()
    {
        return new ModelAndView("login");
    }

    /**
     * 注册页路由
     * @return
     */
    @GetMapping("/register")
    public ModelAndView register()
    {
        return new ModelAndView("register");
    }


    @GetMapping("/forgetPassword")
    public ModelAndView forgetPassword()
    {
        return new ModelAndView("forgetPassword");
    }

    /**
     * 异步查询重名
     * @param name
     * @return
     */
    @GetMapping("/check")        //查看是否有重名
    public Map check(String name)
    {
        Map<String,Boolean> anw=new HashMap<>();
        anw.put("exists",userService.checkName(name));
        return anw;
    }

    /**
     * 查看是否有重邮箱
     * 限刷方式:十秒限刷十次,每秒限刷一次
     * @param email
     * @return
     */
    @AccessLimit(maxCount = 10,seconds = 10)
    @GetMapping("/checkEmail")
    public Map checkEmail(String email)
    {
        Map<String,Boolean> anw=new HashMap<>();
        anw.put("exist",userService.checkEmail(email));
        return anw;
    }


    /**
     * 新增用户
     * @param u
     * @param bindingResult
     * @param redirectAttributes
     * @param session
     * @param response
     * @throws IOException
     */
    @PostMapping("/addUser")
    public void addUser(@Valid User u, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response) throws IOException {
        if(bindingResult.hasErrors())
        {
            redirectAttributes.addFlashAttribute("message",bindingResult.getFieldError().getDefaultMessage());
            response.sendRedirect("/user/register");
        }else{
            String password=u.getPassword();
            userService.add(u);         //我忘了这里是否直接给user直接set上id...
            u.setPassword(password);
            session.setAttribute("user",u);
            session.setAttribute("uid",u.getId());
            session.setAttribute("admin",u.getRole().equals("admin"));
            redirectAttributes.addFlashAttribute("message","账户注册成功!!!");
            response.sendRedirect("/userAdmin/articleList");       //restController下正常重定向无效
        }
    }

    /**
     * 发送找回密码验证码到邮箱
     * @param email
     */
    @PostMapping("/sendEmailCode")
    public void sendEmailCode(String email){
        userService.sendCheckCodeTo(email);
    }

    /**
     * 修改密码用户校验
     * @return
     */
    @AccessLimit(maxCount = 10,seconds = 10)  //接口限刷,每秒一次
    @PostMapping("/forgetAndChangeUser")
    public Map<String,Object> forgetAndChangeUser(User user){
        System.out.println(user);
        Map<String,Object> anw=new HashMap<>();
        boolean success=userService.forgetPasswordAndChange(user);
        if(success==true){
            anw.put("success",success);
            anw.put("message","修改密码成功！！");
        }else{
            anw.put("message","修改失败,请检查姓名与邮箱或者验证码是否过期");
        }
        return anw;
    }


    /**
     * 注销用户
     * @param session
     * @return
     */
    @PostMapping("/exit")      //注销
    public Map<String,String> delete(HttpSession session)
    {
        Map<String,String> anw=new HashMap<>();
        if(session.getAttribute("user")!=null)
        {
            session.removeAttribute("user");
            session.removeAttribute("uid");
            anw.put("message","注销成功!");
        }else{
            anw.put("message","您尚未登录");
        }
        return anw;
    }


}
