package com.example.xhbblog.utils;

import com.github.pagehelper.PageInfo;

public class PageInfoUtil {
    public static PageInfo get(PageInfo pageInfo,int start,int count)
    {
        pageInfo.setHasNextPage((start+count)<pageInfo.getTotal());
        pageInfo.setHasPreviousPage(start-count>=0);
        pageInfo.setPageNum(start/count+1);
        pageInfo.setPageSize(count);
        return pageInfo;          //pageInfo失效的工具类
    }
}
