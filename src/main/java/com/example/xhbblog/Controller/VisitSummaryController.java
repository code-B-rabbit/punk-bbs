package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class VisitSummaryController {

    @Autowired
    private LogService logService;

    @GetMapping("/visitSum")
    public String sumPage(Model model)
    {
        //System.out.println(logService.findLastLogs());
        model.addAttribute("logs",logService.findLastLogs());
        return "visitSum";
    }

}
