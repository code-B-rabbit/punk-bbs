package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Message;

import java.util.List;

/**
 * 留言业务接口
 */
public interface MessageService {
    public void add(Message message);
    public void delete(Integer id);
    public void update(Message message);
    public Message get(Integer id);
    public List<Message> list();
    public Integer count();
}
