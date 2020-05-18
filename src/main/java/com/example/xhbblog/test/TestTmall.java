package com.example.xhbblog.test;

import com.example.xhbblog.Service.*;
import com.example.xhbblog.XhbBlogApplication;
import com.example.xhbblog.configration.BlogConfig;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.util.QiniuUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;
import java.util.Set;

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


    @Autowired
    private RedisTemplate redisTemplate;




}
