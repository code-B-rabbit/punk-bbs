package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.TagService;
import com.example.xhbblog.pojo.Tag;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping("/tagList")
    public String list(@RequestParam(name = "start",defaultValue = "0") Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model)
    {
        PageHelper.offsetPage(start,count);
        List<Tag> tags=tagService.list();
        model.addAttribute("page",new PageInfo<Tag>(tags));
        return "admin/tagList";
    }

    @RequestMapping("/editTag")
    public String edit(Integer id,Model model) //标签id
    {
        model.addAttribute("tag",tagService.get(id));
        return "admin/tagEdit";
    }

    @RequestMapping("/saveTag")
    public String add(Tag tag)
    {
        if(tag.getId()!=null)
        {
            tagService.update(tag);
        }else
        {
            tagService.add(tag);
        }
        return "redirect:/admin/tagList";
    }

    @RequestMapping("/deleteTag")
    public String delete(Integer id)
    {
        tagService.delete(id);
        return "redirect:/admin/tagList";
    }


    @RequestMapping("/articlesOfTag")
    public String showArticles(Integer id)
    {
        return "redirect:/admin/articleListByTag?tid="+id;
    }

}
