package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.ThumbsService;
import com.example.xhbblog.pojo.Thumbs;
import com.example.xhbblog.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ThumbController {

    @Autowired
    private ThumbsService thumbsService;

    @PostMapping("thumbsUp/{aid}")
    public void thumbsUp(@PathVariable("aid") Integer aid, HttpServletRequest request)
    {
        Thumbs thumbs = new Thumbs();
        thumbs.setAid(aid);
        thumbs.setAddress(IpUtil.getIpAddr(request));
        thumbsService.insert(thumbs);
    }

    @PostMapping("thumbsDown/{aid}")
    public void thumbsDown(@PathVariable("aid") Integer aid, HttpServletRequest request)
    {
        thumbsService.deleteThumb(aid,IpUtil.getIpAddr(request));
    }

}
