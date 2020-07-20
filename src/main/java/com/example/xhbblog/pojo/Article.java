package com.example.xhbblog.pojo;


import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {

    private Integer id;

    private Integer tid;

    private Integer uid;

    private Date createTime;

    private String title;

    private String firstPicture;

    private Long visit=new Long(0);        //默认值

    private Article lastArticle;

    private Article nextArticle;

    public Article getLastArticle() {
        return lastArticle;
    }

    public void setLastArticle(Article lastArticle) {
        this.lastArticle = lastArticle;
    }

    public Article getNextArticle() {
        return nextArticle;
    }

    public void setNextArticle(Article nextArticle) {
        this.nextArticle = nextArticle;
    }

    private Tag tag;

    private User user;

    private Integer commentSize;

    private Integer thumbsCount=0;   //默认为零

    private Boolean thumb;

    private Boolean top=false;      //默认为不置顶

    private Boolean published;


    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Boolean getThumb() {
        return thumb;
    }

    public void setThumb(Boolean thumb) {
        this.thumb = thumb;
    }

    public Integer getThumbsCount() {
        return thumbsCount;
    }

    public void setThumbsCount(Integer thumbsCount) {
        this.thumbsCount = thumbsCount;
    }

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

    public Boolean getTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}