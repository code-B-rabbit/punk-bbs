package com.example.xhbblog.test;

import com.example.xhbblog.Service.*;
import com.example.xhbblog.XhbBlogApplication;
import com.example.xhbblog.configration.BlogConfig;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.pojo.*;
import com.example.xhbblog.util.QiniuUtil;
import com.qiniu.common.QiniuException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = XhbBlogApplication.class)
public class TestTmall {

    @Autowired
    private FriendLyLinkService service;

    @Autowired
    private UserService userService;

    @Autowired
    private QiniuUtil qiniuUtil;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogConfig config;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private PictureService pictureService;


    @Test
    public void delete() throws QiniuException {
        qiniuUtil.delete("0649c33d84ad45daa229c6c0c6e9ebd2.jpg");
    }

    @Test
    public void add()
    {
        System.out.println(pictureService.getId());
    }

}
