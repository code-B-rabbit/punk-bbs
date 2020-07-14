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

    /**
     * 访问统计
     * @param model
     * @return
     */
    @GetMapping("/visitSum")
    public String sumPage(Model model)
    {
        model.addAttribute("logs",logService.findLastLogs());
        return "visitSum";
    }

}
