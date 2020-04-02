package com.example.xhbblog.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class User {
    private Integer id;

    @NotBlank(message = "用户名称不能为空!!!")            //只能对字符串使用的注解
    private String name;

    @NotBlank(message = "用户密码不能为空!!!")
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", remembered=" + remembered +
                '}';
    }

    public Boolean getRemembered() {
        return remembered;
    }

    public void setRemembered(Boolean remembered) {
        this.remembered = remembered;
    }

    private Boolean remembered; //是否记住密码

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
}