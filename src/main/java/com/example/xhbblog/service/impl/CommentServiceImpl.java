package com.example.xhbblog.service.impl;

import com.example.xhbblog.message.WebSocketMessage;
import com.example.xhbblog.service.CommentService;
import com.example.xhbblog.manager.RedisCommentManager;
import com.example.xhbblog.manager.RedisUserManager;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.mapper.CommentMapper;
import com.example.xhbblog.mapper.UserMapper;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.message.MessageUtil;
import com.example.xhbblog.utils.RedisKey;
import com.example.xhbblog.websocket.WebSocketServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional(isolation= Isolation.READ_COMMITTED)
@EnableScheduling           //开启定时器
public class CommentServiceImpl implements CommentService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentMapper mapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCommentManager redisCommentManager;

    @Autowired
    private RedisUserManager redisUserManager;

    public Set<User> setUserAndChild(List<Comment> comments){
        Set<User> childUser=new HashSet<>();
        for (Comment comment : comments) {
            User u=userMapper.findById(comment.getUid());
            comment.setUser(u);
            comment.setReplyUsers(setUserAndChild(mapper.listByCid(comment.getId())));
            if(comment.getAnonymous()){
                User user = new User();
                user.setId(null);
                user.setName("匿名用户");
                u=user;
            }
            childUser.add(u);
        }
        return childUser;
    }

    public List<Comment> setArticle(List<Comment> comments,Article article){
        for (Comment comment : comments) {
            comment.setArticle(article);
        }
        return comments;
    }

    public List<Comment> setArticle(List<Comment> comments){
        for (Comment comment : comments) {
            comment.setArticle(articleMapper.get(comment.getAid()));
        }
        return comments;
    }

    @Override
    public void add(Comment comment) {
        LOG.info("{}添加评论{}",comment.getUid(),comment.getContent());
        redisCommentManager.add(comment);
    }

    @Override
    public void update(Comment comment) {
        mapper.updateByPrimaryKey(comment);
    }

    @Override
    public Comment get(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Comment> list() {
        List<Comment> comments=mapper.list();
        setUserAndChild(comments);
        setArticle(comments);
        return comments;
    }

    @Override
    public List<Comment> findByAid(Integer aid,Integer start,Integer count) {
        LOG.info("查看文章id为{}的所有评论,起始点为{},查询{}条",aid,start,count);
        return redisCommentManager.findByAid(aid,start,count);
    }

    @Override
    public List<Comment> listByAid(Integer aid) {
        LOG.info("查看文章id为{}的所有评论",aid);
        List<Comment> comments=mapper.listByAid(aid);       //给后台查看评论使用的接口
        Article article = articleMapper.get(aid);
        setUserAndChild(comments);
        return setArticle(comments,article);
    }

    @Override
    public Integer countOfArticle(Integer aid) {
        LOG.info("查看文章id为{}的所有首发评论(非回复)",aid);
        return redisCommentManager.countOfArticle(aid);
    }

    @Override
    public Integer countOfComment(Integer aid) {
        LOG.info("查看文章id为{}的所有评论(含回复)",aid);
        return redisCommentManager.countOfComment(aid);
    }

    @Override
    public List<Comment> lastComment() {
        LOG.info("查询最新评论");
        return redisCommentManager.lastComment();
    }

    @Override
    public List<Comment> listByUid(Integer uid) {
        LOG.info("查看用户{}的所有评论",uid);
        List<Comment> comments=mapper.listByUid(uid);       //给后台查看评论使用的接口
        setUserAndChild(comments);
        return setArticle(comments);
    }

    @Override
    public List<Comment> findChilds(Integer cid) {
        LOG.info("查看评论{}的所有评论",cid);
        List<Comment> comments=mapper.listByCid(cid);
        setUserAndChild(comments);
        return setArticle(comments);
    }

    /**
     * 将未读信息读入到comment中
     * 实现目的:当评论某文章时将该评论消息推送给该文章作者,
     * 若为回复某评论则也同时将回复消息提示推送给该评论作者
     * 同时放入各自的redis队列中
     * @param comment
     */
    @Override
    public void sendComment(Comment comment,User user) throws IOException {
        Article article=articleMapper.get(comment.getAid());
        String timeStr= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //若评论的文章就是评论者的则直接忽略
        if(article.getUid()!=user.getId()){
            String messageToAdd= MessageUtil.ArtComment(comment,article,user,timeStr);
            redisUserManager.sendMessageTo(article.getUid(),messageToAdd);
        }
        if(comment.getParentID()!=null){
            LOG.info("{}",comment.getParentID());
            Comment parComment=mapper.selectByPrimaryKey(comment.getParentID());
            User parentUser=userMapper.selectByPrimaryKey(parComment.getUid());
            if(parentUser.getId()!=comment.getUid()){   //自己回复自己除外
                String replyToAdd=MessageUtil.replyComment(comment,user,timeStr);
                redisUserManager.sendMessageTo(parentUser.getId(),replyToAdd);
            }
        }
    }

    /**
     * 级联删除单个评论
     * @param comment
     */
    @Override
    public void delete(Comment comment) {
        Subject subject = SecurityUtils.getSubject();
        User user= (User) subject.getPrincipal();
        boolean hasComment=comment.getUid().equals(user.getId());
        boolean hasAdmin=user.getRole().equals("admin");
        if(user!=null&&(hasAdmin||hasComment ||articleMapper.getTitle(comment.getAid()).getUid().equals(user.getId()))){
            redisCommentManager.delete(comment);
            LOG.info("评论{}被删除",comment.getId());
        }
    }

    @Override
    public void deleteCids(List<Comment> comments) {
        LOG.info("删除评论{}的所有评论",comments);
        for (Comment comment : comments) {
            this.delete(comment);
        }
    }

    @Override
    public Integer count() {
        LOG.info("查询评论数量");
        return mapper.count();
    }

    @Override
    public List<Comment> listAnonymousByUid(Integer uid) {
        LOG.info("查看用户{}的所有匿名评论",uid);
        List<Comment> comments=mapper.listAnonymousByUid(uid);       //给后台查看评论使用的接口
        setUserAndChild(comments);
        return setArticle(comments);
    }

    @Override
    public List<Comment> listAnonymousComment() {
        LOG.info("查询所有匿名评论");
        List<Comment> comments=mapper.listAnonymousComment();       //给后台查看评论使用的接口
        setUserAndChild(comments);
        return setArticle(comments);
    }


    @Scheduled(cron = "0 0 10,14,16 * * ?")  //每天上午10点，下午2点，4点
    public void evit()
    {
        redisCommentManager.evit();  //清除所有相关缓存
    }

}
