package com.example.xhbblog.Service;

import com.example.xhbblog.pojo.Tag;

import java.util.List;

public interface TagService {
    public void add(Tag tag);
    public void delete(Integer id);
    public void update(Tag tag);
    public Tag get(Integer id);
    public List<Tag>  list();
}
