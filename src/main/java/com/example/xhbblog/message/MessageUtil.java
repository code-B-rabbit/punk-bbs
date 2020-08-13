package com.example.xhbblog.message;

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
 * 用于生成推送给redis的消息
 * 并检测是否为匿名
 */
public class MessageUtil {

    /**
     * 用于消息回复放入对应队列的工具类
     * @param comment
     * @return
     */
    public static String ArtComment(Comment comment,Article article,User u,String timeStr){
        String name=u.getName();
        if(comment.getAnonymous()==true){
            name="匿名用户";
        }
        String messageToAdd=timeStr+"  "+name+" 评论了你的文章:"+"<span class='text-primary'>"+article.getTitle()+"</span>"+" 内容为:".concat(
                "<span style='color:red'>" +
                comment.getContent()+"</span>" +
                        "<a onclick='return writeIn();' href='listByAid?aid=" +article.getId()+
                        "'>"+
                        "去看看"+
                        "</a>");  //用于推送到列表;\
        return messageToAdd;
    }

    public static String ArtCommentOnline(Comment comment,Article article,User u,String timeStr){
        String name=u.getName();
        if(comment.getAnonymous()==true){
            name="匿名用户";
        }
        String message=timeStr+"  "+name+" 评论了你的文章:"+article.getTitle()+" :".concat(comment.getContent());    //用于后台字符串实时推送
        return message;
    }

    public static String replyComment(Comment comment,User u,String timeStr){
        String name=u.getName();
        if(comment.getAnonymous()==true){
            name="匿名用户";
        }
        String message=timeStr+"  "+name+"回复了你的评论  回复内容为: "+
                "<span style='color:red'>"+comment.getContent()+ "</span>"+
                "<a onclick='return writeIn();' href='listByCid?cid="+comment.getParentID()+"'>"
                +"去看看"+
                "</a>";
        return message;
    }

    /**
     * 封禁文章
     * @param article
     * @return
     */
    public static String bannedMessage(Article article){
        String message="您的文章: "+"<span style='color:red'>" +article.getTitle()+
                "</span>"+" 已被封禁"+"<a onclick='return writeIn();' href='/article?id=" +article.getId()+
                "'>"
                +"查看详情"+
                "</a>";
        return message;
    }

    /**
     * 解封文章
     * @param article
     * @return
     */
    public static String releaseMessage(Article article){
        String message="恭喜您,您的文章: "+"<span style='color:red'>" +article.getTitle()+
                "</span>"+" 已被解封"+"<a onclick='return writeIn();' href='/article?id=" +article.getId()+
                "'>"
                +"查看详情"+
                "</a>";
        return message;
    }

    /**
     * 申请解封
     * @param article
     * @return
     */
    public static String applyMessage(Article article){
        String message="文章: "+"<span style='color:red'>" +article.getTitle()+
                "</span>"+" 申请解封"+"<a onclick='return writeIn();' href='/article?id=" +article.getId()+
                "'>"
                +"查看文章"+
                "</a>";
        return message;
    }

    public static String replyCommentOnline(Comment comment,User u,String timeStr){
        String name=u.getName();
        if(comment.getAnonymous()==true){
            name="匿名用户";
        }
        String message=timeStr+"  "+name+"回复了你的评论 内容为:"+comment.getContent();
        return message;
    }

}
