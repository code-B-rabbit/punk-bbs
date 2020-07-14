package com.example.xhbblog.Controller;

import com.example.xhbblog.pojo.User;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CookieController {

    /**
     * 若cookie存在呢返回cookie
     * @param name
     * @param password
     * @return
     */
    @GetMapping("/getCookie")
    public User readCookie(@CookieValue(value = "name",defaultValue = "") String name,@CookieValue(value = "password",defaultValue = "") String password) {
        User user = new User();
        user.setPassword(password);
        user.setName(name);
        return user;
    }

}
