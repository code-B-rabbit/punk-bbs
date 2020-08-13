package com.example.xhbblog.service.impl;

import com.example.xhbblog.mapper.BannedInfoMapper;
import com.example.xhbblog.pojo.BannedInfo;
import com.example.xhbblog.pojo.BannedInfoExample;
import com.example.xhbblog.service.BannedInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BannedInfoServiceImpl implements BannedInfoService {

    @Autowired
    private BannedInfoMapper bannedInfoMapper;

    @Override
    public void addBanned(BannedInfo bannedInfo) {
        log.info("增加禁止项{]",bannedInfo);
        bannedInfoMapper.insert(bannedInfo);
    }

    @Override
    public void updateBanned(BannedInfo bannedInfo) {
        log.info("修改禁止项{]",bannedInfo);
        bannedInfoMapper.updateByPrimaryKeySelective(bannedInfo);
    }


    @Override
    public BannedInfo selectBannedById(Integer id) {
        return bannedInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BannedInfo> findAll() {
        BannedInfoExample bannedInfoExample = new BannedInfoExample();
        return bannedInfoMapper.selectByExample(bannedInfoExample);
    }
}
