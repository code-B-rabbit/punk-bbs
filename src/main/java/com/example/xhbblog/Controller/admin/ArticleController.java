package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.ArticleService;
import com.example.xhbblog.Service.TagService;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
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
    private String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model,Boolean published)
    {
        PageHelper.offsetPage(start,count);
        List<ArticleWithBLOBs> articles=service.listAll(published);
        model.addAttribute("page",new PageInfo<ArticleWithBLOBs>(articles));
        if(published!=null)
        {
            model.addAttribute("limit","published="+published);           //若存在筛选则进入添加筛选项
        }
        return "admin/articleList";
    }

    @RequestMapping("/articleListByTag")
    private String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model,Integer tid,Boolean published)
    {
        if(start==0)
        {
            model.addAttribute("tops",service.topArts("....",null));
        }
        model.addAttribute("tag",tagService.get(tid));
        PageHelper.offsetPage(start,count);
        List<ArticleWithBLOBs> articles=service.listByTid(tid,published);   //后台调用标签的管理方法
        model.addAttribute("page",new PageInfo<ArticleWithBLOBs>(articles));
        String limit="tid="+tid;
        if(published!=null)
        {
            limit+="&published="+published;            //添加分页的约束条件
        }
        model.addAttribute("limit",limit);
        return "admin/articleList";
    }

    @RequestMapping("/articleLike")
    public String research(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model,String key
    ,Boolean published)
    {
        PageHelper.offsetPage(start,count);
        List<ArticleWithBLOBs> articles=service.listArticleLike(key,published);
        model.addAttribute("page",new PageInfo<ArticleWithBLOBs>(articles));
        String limit="key="+key;
        if(published!=null)
        {
            limit+="&published="+published;            //添加分页的约束条件
        }
        model.addAttribute("limit",limit);
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
        model.addAttribute("tags",tagService.list());
        model.addAttribute("article",service.findById(id,"dddddd"));
        return "admin/articleEdit";
    }
    //修改

    //这里修改和新增保存在了一个方法里
    @RequestMapping("/articleSave")
    public String save(ArticleWithBLOBs article)
    {
        if(article.getId()==null)         //新增
        {
            article.setCreateTime(new Date());
            service.add(article);
        }else
        {
            service.update(article);
        }
        return "redirect:/admin/articleList";
    }

    @RequestMapping("/deleteArticle")
    public String delete(Integer id,Integer tid)
    {
        service.remove(id);
        if(tid!=null){
            return "redirect:/admin/articleListByTag?tid="+tid;  //路由控制
        }
        return "redirect:/admin/articleList";
    }

    @RequestMapping("/getArticle")
    public String get(Integer id)
    {
        return "redirect:/article?id="+id; //重定向到fore路径下
    }
}
