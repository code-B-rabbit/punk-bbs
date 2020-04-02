package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.User;

public interface UserService {
    public void add(User user);
    public User check(User user);
    public void update(User user);
}
