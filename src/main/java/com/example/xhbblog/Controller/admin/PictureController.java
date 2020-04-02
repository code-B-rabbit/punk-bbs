package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.PictureService;
import com.example.xhbblog.pojo.Picture;
import com.example.xhbblog.util.QiniuUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class PictureController {

    @Autowired
    private PictureService service;

    @Autowired
    private QiniuUtil qiniuUtil;

    @RequestMapping("/imageList")
    private String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model)
    {
        PageHelper.offsetPage(start,count);
        List<Picture> pictures=service.list();
        model.addAttribute("page",new PageInfo<Picture>(pictures));
        return "admin/imageList";           //返回视图
    }

    @RequestMapping("/deleteImage")
    public String delete(Integer id) throws QiniuException {
        service.delete(id);
        qiniuUtil.delete(id.toString());        //key和id一致
        return "redirect:/admin/imageList";        //返回重定向
    }

}
