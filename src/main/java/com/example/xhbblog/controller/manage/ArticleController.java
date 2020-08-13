package com.example.xhbblog.controller.manage;


import com.example.xhbblog.pojo.User;
import com.example.xhbblog.service.*;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import org.apache.ibatis.annotations.Update;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * 所共用的对于文章的控制器
 */
@Controller
@RequestMapping({"/userAdmin","/admin"})
public class ArticleController {

    @Autowired
    public ArticleService articleService;

    @Autowired
    public TagService tagService;

    @Autowired
    public UserService userService;

    @Autowired
    public ArticleBannedService articleBannedService;

    @Autowired
    private BannedInfoService bannedInfoService;


    @ModelAttribute("msgCnt")
    public Long  msgs(@SessionAttribute("uid")Integer uid)
    {
        return userService.msgCnt(uid); //用户所获得的消息个数
    }


    @RequestMapping("/addArticle")
    public String add(Model model)
    {
        model.addAttribute("tags",tagService.list());
        return "admin/articleAdd";
    }
    //增加

    /**
     * 这里文章的修改方法可以给任何用户提供查看自己文章内容的机会
     * 所有传递的user对象也自动加了权限
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/editArticle")
    public String edit(Integer id, Model model)
    {
        model.addAttribute("tags",tagService.list());
        model.addAttribute("article",articleService.findById(id,"dddddd","admin"));
        return "admin/articleEdit";
    }
    //修改

    //这里修改和新增保存在了一个方法里
    @RequestMapping("/articleSave")
    public String save(ArticleWithBLOBs article)
    {
        if(article.getId()==null)         //新增
        {
            articleService.add(article);
        }else
        {
            articleService.update(article);
        }
        return "redirect:articleList";
    }

    @RequestMapping(value = "/deleteArticle",method = RequestMethod.POST)
    public @ResponseBody
    void delete(@RequestParam(value="id", required=true) Integer id, @RequestParam(value="uid", required=true)Integer uid)
    {
        Subject subject = SecurityUtils.getSubject();
        User user= (User) subject.getPrincipal();
        if(user.getRole().equals("admin")||user.getId()==uid){
            articleService.deleteArticle(id,uid);
        }
    }

    @RequestMapping("/getArticle")
    public String get(Integer id)
    {
        return "redirect:/article?id="+id; //重定向到fore路径下
    }


    @GetMapping("/reasonForBanned/{bid}")
    public @ResponseBody Object reasonForBanned(@PathVariable("bid") Integer bid){
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("reason",bannedInfoService.selectBannedById(bid).getReason());
        return objectObjectHashMap;
    }

    @GetMapping("/applyForRelease/{aid}")
    public @ResponseBody void applyForRelease(@PathVariable("aid") Integer aid) throws IOException {
        articleBannedService.applyForRelieve(aid);
    }

}
