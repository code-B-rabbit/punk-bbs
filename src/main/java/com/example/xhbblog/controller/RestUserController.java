package com.example.xhbblog.controller;

import com.example.xhbblog.manager.RedisUserManager;
import com.example.xhbblog.service.UserService;
import com.example.xhbblog.annotation.AccessLimit;
import com.example.xhbblog.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

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
     * 用户加盐注册接口
     * 使用shiro随机生成盐值及密码
     * @param bindingResult
     * @param redirectAttributes
     * @param session
     * @param response
     * @throws IOException
     */
    @PostMapping("/addUser")
    public void addUser(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response) throws IOException {
        if(bindingResult.hasErrors())
        {
            redirectAttributes.addFlashAttribute("message",bindingResult.getFieldError().getDefaultMessage());
            response.sendRedirect("/user/register");
        }else{
            String name =  user.getName();
            String password = user.getPassword();
            name = HtmlUtils.htmlEscape(name);
            user.setName(name);//设置盐值
            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            int times = 2; //设置密码
            String algorithmName = "md5";
            String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();
            user.setSalt(salt);
            user.setPassword(encodedPassword);
            userService.add(user);  //添加到数据库表中

            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getName(),password);
            if(subject.isAuthenticated()==false){   //不排除重复一个号注册别的用户的状况
                try{
                    subject.login(usernamePasswordToken);
                    userService.kickItOff(user.getId(),session.getId());
                    user.setPassword(null);
                    session.setAttribute("user",user);
                    session.setAttribute("uid",user.getId());
                    session.setAttribute("admin",user.getRole().equals("admin"));
                    redirectAttributes.addFlashAttribute("message","账户注册成功!!!");
                }catch (Exception e){
                    redirectAttributes.addFlashAttribute("message","系统故障,注册后登录失败");
                }
            }
            response.sendRedirect("/userAdmin/articleList");       //restController下正常重定向无效
        }
    }

    /**
     * 用户验证
     * 十分钟内只允许十次
     * 使用shiro进行认证授权
     * @param user
     * @param session
     * @return
     */
    @AccessLimit(maxCount = 10,seconds = 600)
    @PostMapping("/check")
    public Map<String,Boolean> checkUser(User user, HttpSession session)
    {
        String name =  user.getName();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        Map<String,Boolean> anw=new HashMap<>();
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getName(),user.getPassword());
        try{
            subject.login(usernamePasswordToken);
            user= (User) subject.getPrincipal();
            userService.kickItOff(user.getId(),session.getId());
            anw.put("message",true);
            anw.put("adm",user.getRole().equals("admin"));
            session.setAttribute("user",user);
            session.setAttribute("uid",user.getId());
        }catch (Exception exception){
            exception.printStackTrace();
            anw.put("message",false);
        }
        return anw;
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
        userService.downLine((Integer) session.getAttribute("uid"));
        Map<String,String> anw=new HashMap<>();
        Subject subject=SecurityUtils.getSubject();
        subject.logout();
        anw.put("message","注销成功!");
        return anw;
    }


}
