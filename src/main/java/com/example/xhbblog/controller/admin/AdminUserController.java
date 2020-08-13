package com.example.xhbblog.controller.admin;

import com.example.xhbblog.service.UserService;
import com.example.xhbblog.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理员用户管理
 */
@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/userList")
    public String list(@RequestParam(name = "start",defaultValue = "0") Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model)
    {
        model.addAttribute("count",userService.count());
        PageHelper.offsetPage(start,count);
        List<User> list = userService.list();
        model.addAttribute("page",new PageInfo<User>(list));
        return "admin/userList";
    }

    /**
     * 封号业务：
     * 先删除这个id对应的所有的文章,再删除这个ID对应的数据库表文件
     * @param id
     * @return
     */
    @RequestMapping("/deleteUser")
    public String delete(Integer id)
    {
        userService.deleteUser(id);
        return "redirect:/admin/userList";
    }
}
