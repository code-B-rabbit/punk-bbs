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
    public static final String VISITOFTODAY="VisitOfToday";

    public static final String THUMB_CNT="thumbCount::";    //某文章点赞量
    public static final String THUMB_AID_MAP="thumbAid::";

    public static final String COMMENT="comment::";   //评论缓存
    public static final String COUNTOF_ARTICLE="countOfArticle::";  //文章评论数(用于评论页分页)
    public static final String COUNTOF_COMMENT="countOfComment::";   //文章评论加回复数
    public static final String LAST_COMMENT="lastComment";
    public static final String NOT_READ_COMMENT_LIST="not_read_comments::";//未读评论队列

    public static final String LAST_LOGS="findLastLogs";

    public static final String USR="user::user_";
    public static final String USR_EMAIL="emailCode::";
    public static final String USR_NAME="user::name_";

    public static final String TAG="tag::";
    public static final String TAG_LIST="tag::tagList";

}
