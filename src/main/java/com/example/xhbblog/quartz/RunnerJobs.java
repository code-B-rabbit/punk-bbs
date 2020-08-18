package com.example.xhbblog.quartz;

import com.example.xhbblog.quartz.jobs.CommentJob;
import com.example.xhbblog.quartz.jobs.ThumbJob;
import com.example.xhbblog.quartz.jobs.VisitOfArticleJob;
import com.example.xhbblog.quartz.jobs.VisitOfSiteJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RunnerJobs implements CommandLineRunner {

    @Autowired
    private QuartzService quartzService;

    @Override
    public void run(String... args)  {
        Map<String, Object> paramMap = new HashMap<>();
        quartzService.deleteJob("thumbClear","thumb");
        quartzService.deleteJob("visitCacheClear","visit");
        quartzService.deleteJob("visitOfSiteCacheClear","visitOfSite");
        quartzService.deleteJob("commentClear","commentOfSite");
        quartzService.addJob(ThumbJob.class,"thumbClear","thumb","0 0 1,14 * * ?",paramMap);
        quartzService.addJob(VisitOfArticleJob.class,"visitCacheClear","visit","0 0 1 * * ?",paramMap);
        quartzService.addJob(VisitOfSiteJob.class,"visitOfSiteCacheClear","visitOfSite","0 59 23 * * ?",paramMap);
        quartzService.addJob(CommentJob.class,"commentClear","commentOfSite","0 0 10,14,16 * * ?",paramMap);
        log.info("系统初始任务启动....");
    }

}
