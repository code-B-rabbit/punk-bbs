package com.example.xhbblog.Service;

import com.example.xhbblog.mapper.FriendlyLinkMapper;
import com.example.xhbblog.pojo.FriendlyLink;
import com.example.xhbblog.pojo.FriendlyLinkExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FrientLyLinkServiceImpl implements FriendLyLinkService{

    @Autowired
    private FriendlyLinkMapper mapper;

    @Override
    public void add(FriendlyLink friendlyLink) {
        mapper.insert(friendlyLink);
    }

    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(FriendlyLink friendlyLink) {
        mapper.updateByPrimaryKey(friendlyLink);
    }

    @Override
    public FriendlyLink get(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<FriendlyLink> list() {
        FriendlyLinkExample example=new FriendlyLinkExample();
        example.setOrderByClause("id desc");
        return mapper.selectByExample(example);
    }

    @Override
    public List<FriendlyLink> ListOf(Boolean b) {
        FriendlyLinkExample example=new FriendlyLinkExample();
        example.setOrderByClause("id desc");
        example.createCriteria().andAllowedEqualTo(b);
        return mapper.selectByExample(example);
    }
}
