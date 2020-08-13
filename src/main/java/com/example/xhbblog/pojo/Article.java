package com.example.xhbblog.pojo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Article implements Serializable {

    private static final long serialVersionUID = -6944406518449178907L;

    private Integer id;

    private Integer tid;

    private Integer uid;

    private Integer bid;

    private Date createTime;

    private Date updateTime;

    private String title;

    private String firstPicture;

    private Long visit=new Long(0);        //默认值

    private Article lastArticle;

    private Article nextArticle;

    private Tag tag;

    private User user;

    private Integer commentSize;

    private Integer thumbsCount=0;   //默认为零

    private Boolean thumb;

    private Boolean top=false;      //默认为不置顶

    private Boolean published;

    private BannedInfo bannedInfo;
}