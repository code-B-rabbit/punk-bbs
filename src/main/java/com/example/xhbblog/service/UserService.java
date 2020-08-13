package com.example.xhbblog.service;


import com.example.xhbblog.pojo.User;
import org.apache.shiro.session.Session;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 用户相关业务接口
 */
public interface UserService {
    public void add(User user);
    public void update(User user);
    public boolean checkName(String name);
    public boolean checkEmail(String email);
    public Integer count();
    public List<User> list();
    public User get(Integer id);
    public User findByName(String name);
    public Long msgCnt(Integer uid);              //查看消息的个数
    public List<String> getMessages(Integer uid);     //获得所有的推送消息
    public void deleteUser(Integer uid);  //封号业务
    public void addMessages(String[] messages,Integer uid) throws UnsupportedEncodingException;

    //用户找回密码相关业务
    public boolean forgetPasswordAndChange(User user);
    public void sendCheckCodeTo(String email);
    public void kickItOff(Integer uid,String sid) throws IOException;  //将当前登录的用户踢出
    public void downLine(Integer uid);
}
