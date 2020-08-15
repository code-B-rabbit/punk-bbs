package com.example.xhbblog.service;

import com.example.xhbblog.pojo.Article;
import com.sun.mail.imap.AppendUID;

import java.io.IOException;

/**
 * 文章封禁业务类
 */
public interface ArticleBannedService {
    public void bannedArticle(Integer aid,Integer bid) throws IOException;
    public void applyForRelieve(Integer aid) throws IOException;
    public void relieveArticle(Integer aid) throws IOException;
}
