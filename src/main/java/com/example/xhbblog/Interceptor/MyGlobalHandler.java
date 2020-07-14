package com.example.xhbblog.Interceptor;

import com.example.xhbblog.Service.FriendLyLinkService;
import com.example.xhbblog.Service.LogService;
import com.example.xhbblog.pojo.FriendlyLink;
import com.example.xhbblog.utils.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice        //全局controller增强
@ComponentScan
public class MyGlobalHandler {

    @Autowired
    private FriendLyLinkService friendLyLinkService;

    @Autowired
    private LogService logService;


    @ModelAttribute("fls")
    public List<FriendlyLink> friendlyLinks() {
        return friendLyLinkService.ListOf(true);
    }

    @ModelAttribute("visit")
    public String addIp(HttpServletRequest request) {
        String ipAddr = IpUtil.getIpAddr(request);
        logService.checkAndAdd(ipAddr);
        return ipAddr;
    }


}