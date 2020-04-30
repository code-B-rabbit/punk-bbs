package com.example.xhbblog.Service;


import com.example.xhbblog.pojo.User;

import java.util.List;

public interface UserService {
    public void add(User user);
    public User check(User user);
    public void update(User user);
    public boolean checkName(String name);
    public boolean checkEmail(String email);
    public List<User> list();
    public void delete(Integer id);
    public User get(Integer id);
}
