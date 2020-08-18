package com.example.xhbblog.quartz.jobs;

import com.example.xhbblog.service.ArticleService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class VisitOfArticleJob extends QuartzJobBean {

    @Autowired
    private ArticleService articleService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        articleService.initCache();
    }
}
