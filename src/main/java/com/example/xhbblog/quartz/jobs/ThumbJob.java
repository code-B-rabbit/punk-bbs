package com.example.xhbblog.quartz.jobs;

import com.example.xhbblog.service.ThumbsService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ThumbJob extends QuartzJobBean {
    @Autowired
    private ThumbsService thumbsService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        thumbsService.redisDataToMySQL();
    }
}
