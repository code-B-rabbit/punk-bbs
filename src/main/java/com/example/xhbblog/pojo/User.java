package com.example.xhbblog.pojo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;



public class User implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String password;

    private Integer id;

    private Integer commentSize=0;       //默认为零

    private Boolean checked;               //是否记住密码(传参用)

    private Integer articleSize=0;

    private String checkCode;    //用户用于邮件服务的验证码

    private String role;

    private String email;

    private String headSculpture;

    public String getHeadSculpture() {
        return headSculpture;
    }

    public void setHeadSculpture(String headSculpture) {
        this.headSculpture = headSculpture;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public Integer getArticleSize() {
        return articleSize;
    }

    public void setArticleSize(Integer articleSize) {
        this.articleSize = articleSize;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                ", commentSize=" + commentSize +
                ", checked=" + checked +
                ", articleSize=" + articleSize +
                ", checkCode='" + checkCode + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", headSculpture='" + headSculpture + '\'' +
                '}';
    }


    /**
     * 用于后台查询时使用set对用户对象去重的代码
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    /**
     * 用于后台查询时使用set对用户对象去重的代码
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}