package com.example.xhbblog.test;

import com.example.xhbblog.Service.*;
import com.example.xhbblog.Service.impl.ThumbsServiceImpl;
import com.example.xhbblog.XhbBlogApplication;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.mapper.LogMapper;
import com.example.xhbblog.mapper.ThumbsMapper;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.utils.QiniuUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private ThumbsMapper thumbsMapper;


    @Autowired
    JavaMailSender javaMailSender;

    @Test
    public void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("这是一封测试邮件");
        message.setFrom("511928849@qq.com");
        message.setTo("511928849@qq.com");
        message.setSentDate(new Date());
        message.setText("这是测试邮件的正文");
        javaMailSender.send(message);
    }

    public static volatile int cnt=0;
}
