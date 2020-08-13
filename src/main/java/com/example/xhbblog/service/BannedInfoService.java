package com.example.xhbblog.service;


import com.example.xhbblog.pojo.BannedInfo;

import java.util.List;

/**
 * 封禁项管理
 */
public interface BannedInfoService {
    public void addBanned(BannedInfo bannedInfo);
    public void updateBanned(BannedInfo bannedInfo);
    public BannedInfo selectBannedById(Integer id);
    public List<BannedInfo> findAll();
}
