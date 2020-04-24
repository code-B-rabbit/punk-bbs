package com.example.xhbblog.pojo;

import java.util.Date;
import java.util.List;

public class Comment {
    private Integer id;

    private Integer aid;

    private Article article;   //映射的文章

    private String parentVisitorName;   //父评论的用户名

    private List<Comment> childs;     //该评论的回复

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

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    private Date createTime;

    private String visitor_name;

    private String visitor_email;

    private Integer parentID;

    private String content;

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

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name == null ? null : visitor_name.trim();
    }

    public String getVisitor_email() {
        return visitor_email;
    }

    public void setVisitor_email(String visitor_email) {
        this.visitor_email = visitor_email == null ? null : visitor_email.trim();
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}