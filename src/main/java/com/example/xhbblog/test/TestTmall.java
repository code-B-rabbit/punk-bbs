package com.example.xhbblog.test;

import com.example.xhbblog.Service.*;
import com.example.xhbblog.Service.impl.ThumbsServiceImpl;
import com.example.xhbblog.XhbBlogApplication;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.mapper.LogMapper;
import com.example.xhbblog.mapper.ThumbsMapper;
import com.example.xhbblog.pojo.Thumbs;
import com.example.xhbblog.utils.QiniuUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Target;
import java.time.LocalDateTime;
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
    public void redisDataToMySQL() {
        logger.info("time:{}，开始执行Redis数据持久化到MySQL任务", LocalDateTime.now());
        //1.更新文章总的点赞数
        Set<String> keys = redisTemplate.keys("exist:*");
        for (String key : keys) {
            Integer aid=Integer.parseInt(key.substring(6));
            List<String> thumbs = thumbsMapper.ipOfAid(aid);
            if(thumbs.size()!=0){
                for (String thumb : thumbs) {
                    redisTemplate.opsForSet().add("mysql:"+aid,thumb);
                }
            }
            redisTemplate.opsForSet().intersectAndStore("mysql:"+aid,"thumbs:"+aid,"union");//取到一个并集
            System.out.println(redisTemplate.opsForSet().members("union").size());
            Set<String> toRemove = redisTemplate.opsForSet().difference( "mysql:" + aid,"union");//mysql中与并集不相符的全部删掉
            //System.out.println(toRemove.size());
            removeOld(toRemove,aid);
            Set<String> toAdd=redisTemplate.opsForSet().difference("thumbs:"+aid,"union");
            addId(toAdd,aid);

            redisTemplate.delete(key);
            redisTemplate.delete("mysql:"+aid);
            redisTemplate.delete("thumbs:"+aid);
            redisTemplate.delete("union");
        }
        logger.info("time:{}，结束执行Redis数据持久化到MySQL任务", LocalDateTime.now());
    }

    /**
     * 删除所有取消赞的项
     * @param toRemove
     * @param aid
     */
    private void removeOld(Set<String> toRemove,Integer aid){
        for (String s : toRemove) {
            thumbsMapper.deleteThumb(aid,s);
        }
    }

    private void addId(Set<String> toAdd,Integer aid){
        for (String s : toAdd) {
            Thumbs thumbs=new Thumbs();
            thumbs.setAddress(s);
            thumbs.setAid(aid);
            thumbsMapper.insert(thumbs);
        }
    }
}
