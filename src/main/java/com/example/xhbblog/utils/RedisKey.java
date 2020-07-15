package com.example.xhbblog.utils;

import com.example.xhbblog.mapper.*;
import com.example.xhbblog.pojo.Thumbs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 记录没有使用spring boot cache的key工具类
 */
@Component
@Transactional
@EnableScheduling
public  class RedisKey {

    public static final String ART_VISITS_ZSET="visits";   //文章访问排行使用ZSET数据结构,并进行实时更新
    public static final String ART_VISIT="article::";
    public static final String ART_TOP="top";        //置顶文章
    public static final String LAST_ARTICLE="LastArticle";  //最新文章
    public static final String THUMBS_SET="thumbs:";  //某一个文章的点赞缓存(一个记录点赞ip地址的set)
    public static final String THUMB_EXIST="exist:";    //某一个文章是否被点赞了
    public static final String THUMB_TOMYSQL="mysql:";
    public static final String THUMB_SET_UNION="union";
    public static final String  VISITOFTODAY="VisitOfToday";
    public static final String COMMENT="comment::";   //评论缓存
    public static final String NOT_READ_COMMENT_LIST="not_read_comments::";//未读评论队列


    public static final String COUNTOF_ARTICLE="countOfArticle";  //文章评论数(用于评论页分页)
    public static final String COUNTOF_COMMENT="countOfComment";   //文章评论加回复数
    public static final String USR_NO_NAME="userNoName";
    public static final String USR="user::";

    public static final String LAST_COMMENT="lastComment";
    public static final String USR_EMAIL="emailCode::";

}
