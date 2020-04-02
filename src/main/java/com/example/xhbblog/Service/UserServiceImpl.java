package com.example.xhbblog.Service;

import com.example.xhbblog.mapper.UserMapper;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.pojo.UserExample;
import com.example.xhbblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public void add(User u) {
        u.setPassword(MD5Utils.code(u.getPassword())); //加密
        userMapper.insert(u);
    }

    @Override
    public User check(User user){
        user.setPassword(MD5Utils.code(user.getPassword()));  //加密
        UserExample userExample=new UserExample();
        userExample.createCriteria().andNameEqualTo(user.getName())
                .andPasswordEqualTo(user.getPassword());
        List<User> users=userMapper.selectByExample(userExample);
        return users.isEmpty()?null:users.get(0);
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKey(user);
    }
}
