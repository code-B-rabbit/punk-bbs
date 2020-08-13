package com.example.xhbblog.shiro;

import com.example.xhbblog.pojo.User;
import com.example.xhbblog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject= SecurityUtils.getSubject();//从subject里拿到user
        User user= (User) subject.getPrincipal();//设置角色
        HashSet<String> roles = new HashSet<>();//设置角色
        roles.add(user.getRole());
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.setRoles(roles);
        return info;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken= (UsernamePasswordToken) authenticationToken;
        User user= userService.findByName(usernamePasswordToken.getUsername());
        String salt=user.getSalt();
        if(user!=null){
            return new SimpleAuthenticationInfo(user,user.getPassword(), ByteSource.Util.bytes(salt),getName());   //由shiro来做认证逻辑
        }
        return null;
    }
}
