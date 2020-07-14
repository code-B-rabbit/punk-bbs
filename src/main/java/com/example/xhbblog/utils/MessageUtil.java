package com.example.xhbblog.utils;

import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.mapper.UserMapper;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用于传递websocket各用户间推送消息的自己编写的工具类
 */
public class MessageUtil {


    /**
     * 用于消息回复放入对应队列的工具类
     * @param comment
     * @return
     */
    public static String ArtComment(Comment comment,Article article,User u,String timeStr){
        String messageToAdd=timeStr+"  "+u.getName()+" 评论了你的文章:"+"<span class='text-primary'>"+article.getTitle()+"</span>"+" 内容为:".concat(
                "<span style='color:red'>" +
                comment.getContent()+"</span>" +
                        "<a onclick='return writeIn();' href='listByAid?aid=" +article.getId()+
                        "'>"+
                        "去看看"+
                        "</a>");  //用于推送到列表;\
        return messageToAdd;
    }

    public static String ArtCommentOnline(Comment comment,Article article,User u,String timeStr){
        String message=timeStr+"  "+u.getName()+" 评论了你的文章:"+article.getTitle()+" :".concat(comment.getContent());    //用于后台字符串实时推送
        return message;
    }

    public static String replyComment(Comment comment,User u,String timeStr){
        String message=timeStr+"  "+u.getName()+"回复了你的评论  内容为:"+comment.getContent()+" "+
                "<a onclick='return writeIn();' href='listByCid?cid="+comment.getParentID()+"'>"
                +"去看看"+
                "</a>";
        return message;
    }

    public static String replyCommentOnline(Comment comment,User u,String timeStr){
        String message=timeStr+"  "+u.getName()+"回复了你的评论 内容为:"+comment.getContent();
        return message;
    }
}
