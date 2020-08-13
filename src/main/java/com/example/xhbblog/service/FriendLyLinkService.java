package com.example.xhbblog.service;

import com.example.xhbblog.pojo.FriendlyLink;

import java.util.List;

/**
 * 友链业务接口
 */
public interface FriendLyLinkService {
    public void add(FriendlyLink friendlyLink);
    public void delete(Integer id);
    public void update(FriendlyLink friendlyLink);
    public FriendlyLink get(Integer id);
    public List<FriendlyLink> list();
    public List<FriendlyLink> ListOf(Boolean b);     //通过同意以及不同意来进行查询
    public Integer count();
}
