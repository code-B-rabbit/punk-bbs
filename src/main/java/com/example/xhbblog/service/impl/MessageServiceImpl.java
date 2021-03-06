package com.example.xhbblog.service.impl;

import com.example.xhbblog.service.MessageService;
import com.example.xhbblog.mapper.MessageMapper;
import com.example.xhbblog.pojo.Message;
import com.example.xhbblog.pojo.MessageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(isolation= Isolation.READ_COMMITTED)
@CacheConfig(cacheNames = "message")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper mapper;

    @Override
    public void add(Message message) {
        mapper.insert(message);
    }

    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Message message) {
        mapper.updateByPrimaryKey(message);
    }

    @Override
    public Message get(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Message> list() {
        MessageExample example=new MessageExample();
        example.setOrderByClause("id desc");
        return mapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public Integer count() {
        return mapper.countMessages();
    }
}
