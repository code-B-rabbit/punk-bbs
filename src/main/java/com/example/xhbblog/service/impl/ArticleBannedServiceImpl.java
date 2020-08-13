package com.example.xhbblog.service.impl;

import com.example.xhbblog.manager.RedisUserManager;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.mapper.UserMapper;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.service.ArticleBannedService;
import com.example.xhbblog.message.MessageUtil;
import com.example.xhbblog.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ArticleBannedServiceImpl implements ArticleBannedService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUserManager redisUserManager;


    @Override
    public void bannedArticle(Integer bid, Integer aid) throws IOException {
        Article article=articleMapper.get(aid);
        articleMapper.setArticleBanned(aid,bid);
        String message=MessageUtil.bannedMessage(article);
        redisUserManager.sendMessageTo(article.getUid(), message);
        log.info("封禁文章id:{},封禁项id:{}",article.getTitle(),bid);
    }

    @Override
    public void applyForRelieve(Integer aid){
        Article article=articleMapper.get(aid);
        List<User> users = userMapper.selectAdmin();
        String message=MessageUtil.applyMessage(article);
        for (User user : users) {
            redisUserManager.sendMessageTo(user.getId(),message);
        }
        log.info("文章:{}--->请求解禁",article.getTitle());
    }

    @Override
    public void relieveArticle(Integer aid) throws IOException {
        articleMapper.setArticleRelease(aid);
        Article article = articleMapper.get(aid);
        String message=MessageUtil.releaseMessage(article);
        redisUserManager.sendMessageTo(article.getUid(), message);
        log.info("解封文章:{}",article.getTitle());
    }
}
