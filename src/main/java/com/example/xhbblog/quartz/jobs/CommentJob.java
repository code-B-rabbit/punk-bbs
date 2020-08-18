package com.example.xhbblog.quartz.jobs;

import com.example.xhbblog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

@Slf4j
public class CommentJob extends QuartzJobBean {

    @Autowired
    private CommentService commentService;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        commentService.clearCache();
        log.info("{}清除评论缓存", LocalDateTime.now());
    }
}
