package com.example.xhbblog.controller.admin;

import com.example.xhbblog.pojo.BannedInfo;
import com.example.xhbblog.pojo.FriendlyLink;
import com.example.xhbblog.service.BannedInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminBannedInfoController {

    @Autowired
    private BannedInfoService bannedInfoService;

    @PostMapping("/addBannedInfo")
    public String addBanned(@Valid BannedInfo bannedInfo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "redirect:/admin/listBannedInfo";
        }
        bannedInfoService.addBanned(bannedInfo);
        return "redirect:/admin/listBannedInfo";
    }

    @RequestMapping("/listBannedInfo")
    public String listBanned(Model model, @RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count){
        PageHelper.offsetPage(start,count);
        List<BannedInfo> list = bannedInfoService.findAll();
        model.addAttribute("page",new PageInfo<>(list));
        return "admin/bannedList";
    }

    @PostMapping("/updateBannedInfo")
    public @ResponseBody Object updateBanned(@Valid BannedInfo bannedInfo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return false;
        }
        bannedInfoService.updateBanned(bannedInfo);
        return true;
    }

}
