package com.example.xhbblog.service;

import com.example.xhbblog.pojo.Tag;

import java.util.List;

/**
 * 标签业务接口
 */
public interface TagService {
    public Tag add(Tag tag);
    public void delete(Integer id);
    public Tag update(Tag tag);
    public Tag get(Integer id);
    public List<Tag>  list();   //查询所有
    public List<Tag> listBySort();
    public Integer count();
}
