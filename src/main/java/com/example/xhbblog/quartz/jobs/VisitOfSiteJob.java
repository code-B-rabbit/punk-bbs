package com.example.xhbblog.quartz.jobs;

import com.example.xhbblog.service.LogService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class VisitOfSiteJob extends QuartzJobBean {

    @Autowired
    private LogService logService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logService.writeDate();
    }
}
