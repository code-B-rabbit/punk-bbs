package com.example.xhbblog.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Comment implements Serializable {
    private Integer id;

    private Integer aid;

    private Date createTime;

    private Integer parentID;

    private Integer uid;

    private String content;

    private Boolean anonymous;

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    //四个映射类属性
    private Article article;     //多对一映射

    private String visitor_name;


    private Comment parentComment;

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    private List<Comment> childs;

    private User user;  //映射的用户类


    private Set<User> replyUsers;   //评论的全部使用者

    public Set<User> getReplyUsers() {
        return replyUsers;
    }

    public void setReplyUsers(Set<User> replyUsers) {
        this.replyUsers = replyUsers;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public List<Comment> getChilds() {
        return childs;
    }

    public void setChilds(List<Comment> childs) {
        this.childs = childs;
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", aid=" + aid +
                ", createTime=" + createTime +
                ", parentID=" + parentID +
                ", uid=" + uid +
                ", content='" + content + '\'' +
                ", anonymous=" + anonymous +
                ", article=" + article +
                ", visitor_name='" + visitor_name + '\'' +
                ", parentComment=" + parentComment +
                ", childs=" + childs +
                ", user=" + user +
                ", replyUsers=" + replyUsers +
                '}';
    }
}