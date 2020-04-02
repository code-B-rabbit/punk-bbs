package com.example.xhbblog.pojo;

import java.util.Date;

public class Article {
    private Integer id;

    private Integer tid;

    private Date createTime;

    private String title;

    private String firstPicture;

    private Long visit=new Long(0);

    private Boolean published;

    private String content;

    private Integer commentSize=0;

    private Tag tag;

    private String nextName;

    public String getNextName() {
        return nextName;
    }

    public void setNextName(String nextName) {
        this.nextName = nextName;
    }

    public Integer getNextId() {
        return nextId;
    }

    public void setNextId(Integer nextId) {
        this.nextId = nextId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getLastId() {
        return lastId;
    }

    public void setLastId(Integer lastId) {
        this.lastId = lastId;
    }

    private Integer nextId;

    private String lastName;

    private Integer lastId;


    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture == null ? null : firstPicture.trim();
    }

    public Long getVisit() {
        return visit;
    }

    public void setVisit(Long visit) {
        this.visit = visit;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", tid=" + tid +
                ", createTime=" + createTime +
                ", title='" + title + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", visit=" + visit +
                ", published=" + published +
                ", content='" + content + '\'' +
                ", commentSize=" + commentSize +
                ", tag=" + tag +
                ", nextName='" + nextName + '\'' +
                ", nextId=" + nextId +
                ", lastName='" + lastName + '\'' +
                ", lastId=" + lastId +
                '}';
    }
}