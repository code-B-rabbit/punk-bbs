package com.example.xhbblog.websocket;

import com.example.xhbblog.utils.RedisKey;
import com.example.xhbblog.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
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
@Component
@ServerEndpoint(value = "/webSocket/{uid}" )
@Slf4j
public class WebSocketServer {

    private Session session;

    //用于存储用户是否登录状态的哈希表(线程安全类)
   public static ConcurrentHashMap<Integer,WebSocketServer> webSocketMap=new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session,@PathParam(value = "uid") Integer uid){
        this.session=session;
        webSocketMap.put(uid,this);
        log.info("用户{}在线",uid);
        log.info("websocket有新的连接,总数:{}",webSocketMap.size());
    }

    @OnClose
    public void onClose(@PathParam(value = "uid") Integer uid){
        webSocketMap.remove(uid);
        log.info("用户{}离线",uid);
        log.info("websocket连接断开,总数:{}",webSocketMap.size());
    }

    @OnMessage
    public void onMessage(String message){
        log.info("收到客户端发来的消息:{}",message);
    }


    public void sendMessage(Integer uid,String message) throws IOException {
        log.info("发送信息{}",message);
        WebSocketServer webSocketServer=webSocketMap.get(uid);
        if(webSocketServer!=null){
            webSocketServer.session.getAsyncRemote()    //这里采取异步非阻塞的消息发布方式
                    .sendText(message);     //websocker消息异步推送
        }
    }


}