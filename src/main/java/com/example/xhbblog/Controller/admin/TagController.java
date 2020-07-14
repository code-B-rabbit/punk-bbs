package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.TagService;
import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.pojo.Tag;
import com.example.xhbblog.utils.PageInfoUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @ModelAttribute("count")
    public Integer count(){
        return tagService.count();
    }

    @ModelAttribute("msgCnt")
    public Long  msgs(@SessionAttribute("uid")Integer uid)
    {
        return userService.msgCnt(uid); //用户所获得的消息个数
    }

    @RequestMapping("/tagList")
    public String list(@RequestParam(name = "start",defaultValue = "0") Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model)
    {
        List<Tag> tags=tagService.listBySort();
        PageInfo<Tag> tagPageInfo = new PageInfo<>(tags);
        model.addAttribute("page", PageInfoUtil.get(tagPageInfo,start,count));
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
