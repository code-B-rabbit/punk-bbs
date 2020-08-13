package com.example.xhbblog.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class Comment implements Serializable {
    private static final long serialVersionUID = 782815280694621371L;

    private Integer id;

    private Integer aid;

    private Date createTime;

    private Date updateTime;

    private Integer parentID;

    private Integer uid;

    private String content;

    private Boolean anonymous;
    //四个映射类属性
    private Article article;     //多对一映射

    private String visitor_name;

    private Comment parentComment;

    private List<Comment> childs;

    private User user;  //映射的用户类

    private Set<User> replyUsers;   //评论的全部使用者
}