package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.ArticleService;
import com.example.xhbblog.Service.TagService;
import com.example.xhbblog.pojo.Article;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ArticleController {

    @Autowired
    private ArticleService service;

    @Autowired
    private TagService tagService;

    @RequestMapping("/articleList")
    private String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model)
    {
        PageHelper.offsetPage(start,count);
        List<Article> articles=service.listAll();
        model.addAttribute("page",new PageInfo<Article>(articles));
        return "admin/articleList";
    }

    @RequestMapping("/articleListByTag")
    private String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model,Integer tid)
    {
        PageHelper.offsetPage(start,count);
        List<Article> articles=service.listByTid(tid);   //后台调用标签的管理方法
        model.addAttribute("page",new PageInfo<Article>(articles));
        model.addAttribute("limit","tid="+tid);
        return "admin/articleList";
    }

    @RequestMapping("/articleLike")
    public String research(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model,String key)
    {
        PageHelper.offsetPage(start,count);
        List<Article> articles=service.listArticleLike(key);
        model.addAttribute("page",new PageInfo<Article>(articles));
        model.addAttribute("limit","key="+key);
        return "admin/articleList";
    }

    @RequestMapping("/addArticle")
    public String add(Model model)
    {
        model.addAttribute("tags",tagService.list());
        return "admin/articleAdd";
    }
    //增加

    @RequestMapping("/editArticle")
    public String edit(Integer id,Model model)
    {
        //System.out.println(service.findById(id));
        model.addAttribute("tags",tagService.list());
        model.addAttribute("article",service.findById(id));
        return "admin/articleEdit";
    }
    //修改

    //这里保存在了一个方法里
    @RequestMapping("/articleSave")
    public String save(Article article)
    {
        article.setCreateTime(new Date());
        //System.out.println(article.getPublished());
        if(article.getId()==null)
        {
            service.add(article);
        }else
        {
            service.update(article);
        }
        return "redirect:/admin/articleList";
    }

    @RequestMapping("/deleteArticle")
    public String delete(Integer id)
    {
        service.remove(id);
        return "redirect:/admin/articleList";
    }

}
