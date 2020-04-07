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
        for (int i =0; i < 5; i++) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE,i);
            Date now = c.getTime();
            Article article=new Article();
            article.setCreateTime(now);
            article.setTitle("测试(2)"+i);
            article.setPublished(true);
            article.setTid(6);
            articleService.add(article);
        }
    }

    @Test
    public void add()
    {
        System.out.println(pictureService.getId());
    }

}
