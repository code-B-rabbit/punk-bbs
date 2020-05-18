package com.example.xhbblog.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Comment implements Serializable {
    private Integer id;

    private Integer aid;

    private Date createTime;

    private Integer parentID;

    private Integer uid;

    private String content;

    //四个映射类属性
    private Article article;     //多对一映射

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    private String visitor_name;

    private String visitor_email;

    private String parentVisitorName;

    private List<Comment> childs;

    public String getParentVisitorName() {
        return parentVisitorName;
    }

    public void setParentVisitorName(String parentVisitorName) {
        this.parentVisitorName = parentVisitorName;
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

    public String getVisitor_email() {
        return visitor_email;
    }

    public void setVisitor_email(String visitor_email) {
        this.visitor_email = visitor_email;
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
}