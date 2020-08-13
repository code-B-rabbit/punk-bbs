package com.example.xhbblog.test;

import com.example.xhbblog.mapper.ThumbMapper;
import com.example.xhbblog.service.*;
import com.example.xhbblog.service.impl.ThumbsServiceImpl;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.mapper.LogMapper;
import com.example.xhbblog.utils.QiniuUtil;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestBlog {

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
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private PictureService pictureService;


    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private LogMapper logMapper;

    private Logger logger= LoggerFactory.getLogger(ThumbsServiceImpl.class);

    @Autowired
    private ThumbMapper thumbsMapper;


    @Autowired
    JavaMailSender javaMailSender;



    public static volatile int cnt=0;
}
