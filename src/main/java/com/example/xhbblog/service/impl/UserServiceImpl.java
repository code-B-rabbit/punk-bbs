package com.example.xhbblog.service.impl;

import com.example.xhbblog.service.EmailService;
import com.example.xhbblog.service.UserService;
import com.example.xhbblog.manager.RedisArticleManager;
import com.example.xhbblog.manager.RedisCommentManager;
import com.example.xhbblog.manager.RedisUserManager;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.mapper.CommentMapper;
import com.example.xhbblog.mapper.UserMapper;
import com.example.xhbblog.pojo.*;

import com.example.xhbblog.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.annotation.SpringSessionRedisOperations;
import org.springframework.session.web.server.session.SpringSessionWebSessionStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


@Service
@Transactional(isolation= Isolation.READ_COMMITTED)
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisArticleManager redisArticleManager;

    @Autowired
    private RedisCommentManager redisCommentManager;

    @Autowired
    private RedisUserManager redisUserManager;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void add(User u) {
        userMapper.insert(u);
        log.info("{}用户注册",u);
    }


    @Override
    public void update(User user) {
        redisUserManager.update(user);
        log.info("{}用户登录修改用户信息",user);
    }

    @Override
    public boolean checkName(String name) {
        return userMapper.checkName(name)>0;     //大于0则存在
    }

    @Override
    public boolean checkEmail(String email) {
        return userMapper.checkEmail(email)>0;
    }

    @Override
    public Integer count() {
        return userMapper.count();
    }

    @Override
    public List<User> list() {
        return userMapper.selectUser();
    }



    @Override
    public User get(Integer id) {
        return userMapper.findById(id);
    }


    @Override
    public User findByName(String name) {
        if(name.equals("匿名游客")){
            return redisUserManager.findByName(name);
        }
        return userMapper.findByName(name);
    }

    @Override
    public Long msgCnt(Integer uid) {
        return redisUserManager.msgCnt(uid);
    }

    /**
     * 封号业务,同时将该用户所有文章以及该文章所有评论以及相关缓存删除
     * 还要将该用户其他的文章评论同时删除
     * @param uid
     */
    @Override
    public void deleteUser(Integer uid) {
        log.info("删除用户uid为{}",uid);
        redisUserManager.delUsr(uid);
        List<ArticleWithBLOBs> articles=articleMapper.findAll(null,null,uid);
        for (ArticleWithBLOBs article : articles) {
            redisArticleManager.deleteArticle(article.getId(),uid);
        }
        List<Comment> comments=commentMapper.listByUid(uid);
        for (Comment comment : comments) {
            redisCommentManager.delete(comment);
        }
        userMapper.deleteByPrimaryKey(uid);
    }



    /**
     * 获得用户所有推送的信息(博客被留言,被评论等)
     * @return
     */
    @Override
    public List<String> getMessages(Integer uid) {
        return redisUserManager.getMessages(uid);
    }

    /**
     * 读信息后保存没有标记已读的信息的业务
     * @param messages
     * @param uid
     */
    @Override
    public void addMessages(String[] messages, Integer uid) throws UnsupportedEncodingException {
        int length=messages.length;
        if(length!=0) {
            for (int i = 0; i < length; i++) {
                messages[i] = URLDecoder.decode(messages[i], "utf-8");
            }
            log.info("用户{}添加信息{}", uid, messages);
            redisUserManager.addMessages(uid, messages);
        }
    }

    /**
     * 先去寻找验证码校验是否过期,如果过期直接修改失败
     * 未过期则去数据库校验该姓名与邮箱是否存在,不存在也直接修改失败
     * 若存在则通过update方法修改数据库user表
     * @param user
     * @return
     */
    @Override
    public boolean forgetPasswordAndChange(User user) {
        String code=redisUserManager.getEmailCode(user.getEmail());
        if(code==null){
            return false;
        }
        Integer uid=userMapper.checkUserExist(user.getName(),user.getEmail());
        boolean anw=(uid!=null);
        anw&=(user.getCheckCode()).equals(code);  //还要保证验证码与邮件验证码相等
        if(anw==true){
            user.setId(uid);
            String name =  user.getName();
            String password = user.getPassword();
            name = HtmlUtils.htmlEscape(name);
            user.setName(name);//设置盐值
            String salt = new SecureRandomNumberGenerator().nextBytes().toString();

            //设置密码
            int times = 2;
            String algorithmName = "md5";
            String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();
            user.setSalt(salt);
            user.setPassword(encodedPassword);
            userMapper.updateByPrimaryKeySelective(user);   //修改数据库
        }
        return anw;
    }

    /**
     * 在redis服务中生成验证码并存储30分钟,通过邮件服务发给所输入的邮箱
     * @param email
     */
    @Override
    public void sendCheckCodeTo(String email) {
       String code=redisUserManager.setEmailCode(email);       //所生成的验证码
       emailService.sendEmail("找回密码验证码",email,"您的验证码为"+code+"请尽快修改,期限为30分钟");
    }
    

    @Override
    public void kickItOff(Integer uid,String sid) throws IOException {
        String lastSid= (String) redisUserManager.loadShiroSession(uid);
        if(lastSid!=null&&!lastSid.equals(sid)){
            log.info("异地登录!!!");
            redisUserManager.sendMessageTo(uid,"账号异地登录!!!您已被迫下线");  //通过redisSessionReposity获取session对象
            RedisSessionRepository redisSessionRepository=new RedisSessionRepository(redisTemplate);
            redisSessionRepository.deleteById(lastSid);
        }
        redisUserManager.putShiroSession(uid,sid);
    }

    @Override
    public void downLine(Integer uid) {
        redisUserManager.deleteShiroSession(uid);
    }


}
