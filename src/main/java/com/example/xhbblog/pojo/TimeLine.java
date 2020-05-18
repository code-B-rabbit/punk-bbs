package com.example.xhbblog.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TimeLine implements Serializable {
    private Date time;         //哪一天
    private List<Article> articleList;

    @Override
    public String toString() {
        return "TimeLine{" +
                "time=" + time +
                ", articleList=" + articleList +
                '}';
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
