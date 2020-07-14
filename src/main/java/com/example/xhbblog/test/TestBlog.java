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
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = XhbBlogApplication.class)
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


    @Test
    public void test() throws UnsupportedEncodingException {
        System.out.println(URLDecoder.decode("2020-07-14%2012%3A05%3A20%20%20%E5%8C%BF%E5%90%8D%E7%94%A8%E6%88%B7%20%E8%AF%84%E8%AE%BA%E4%BA%86%E4%BD%A0%E7%9A%84%E6%96%87%E7%AB%A0%3A%3Cspan%20class%3D'text-primary'%3E%E6%9A%B4%E8%B7%B3%2C%E5%A6%82%E9%9B%B7%3C%2Fspan%3E%20%E5%86%85%E5%AE%B9%E4%B8%BA%3A%3Cspan%20style%3D'", "UTF-8"));


    }
}
