package com.example.xhbblog.websocket;

import com.example.xhbblog.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 相当于websocket的controller
 * 无法依赖注入:websocket不是单例的,底层不受spring管理
 */
@ServerEndpoint(value = "/webSocket/{uid}" )
@Component
@ComponentScan
public class WebSocketServer {

    private Session session;

    //用于存储用户是否登录状态的哈希表(线程安全类)
    public static ConcurrentHashMap<Integer,WebSocketServer> webSocketMap=new ConcurrentHashMap<>();


    private Logger LOG= LoggerFactory.getLogger(WebSocketServer.class);

    @OnOpen
    public void onOpen(Session session,@PathParam(value = "uid") Integer uid){
        this.session=session;
        webSocketMap.put(uid,this);
        LOG.info("用户{}在线",uid);
        LOG.info("websocket有新的连接,总数:{}",webSocketMap.size());
    }

    @OnClose
    public void onClose(@PathParam(value = "uid") Integer uid){
        webSocketMap.remove(uid);
        LOG.info("用户{}离线",uid);
        LOG.info("websocket连接断开,总数:{}",webSocketMap.size());
    }

    @OnMessage
    public void onMessage(String message){
        LOG.info("收到客户端发来的消息:{}",message);
    }

    public void sendMessage(Integer uid,String message) throws IOException {
        LOG.info("发送信息{}",message);
        WebSocketServer webSocketServer=webSocketMap.get(uid);
        if(webSocketServer!=null){
            webSocketServer.session.getBasicRemote()
                    .sendText(message);
        }
    }


}