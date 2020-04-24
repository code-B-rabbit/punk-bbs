package com.example.xhbblog.test;

import com.example.xhbblog.Service.*;
import com.example.xhbblog.XhbBlogApplication;
import com.example.xhbblog.configration.BlogConfig;
import com.example.xhbblog.mapper.ArticleMapper;
import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.util.QiniuUtil;
import com.qiniu.common.QiniuException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = XhbBlogApplication.class)
public class TestTmall {

    @Autowired
    private FriendLyLinkService service;

    @Autowired
    private UserService userService;

    @Autowired
    private QiniuUtil qiniuUtil;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogConfig config;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private PictureService pictureService;


    @Test
    public void add()
    {
        System.out.println(pictureService.getId());
    }

    @Test
    public void judgeIp() throws IOException {
        InetAddress host = InetAddress.getLocalHost();
        String ip =host.getHostAddress();
        if(isReachable(ip))
        {
            System.out.println(ip);
        }
        for (int i = 0; i < 10; i++) {
            String s=ip.substring(ip.lastIndexOf(".")+1,ip.length());
            ip=ip.substring(0,ip.lastIndexOf(".")+1);
            s=String.valueOf(Integer.parseInt(s)+1);
            ip+=s;
            if(isReachable(ip))
            {
                System.out.println(ip);
            }
        }        
    }

    private static boolean isReachable(String ip) {
        try {
            boolean reachable = false;

            Process p = Runtime.getRuntime().exec("ping -n 1 " + ip);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.length() != 0)
                    sb.append(line + "\r\n");
            }

            //当有TTL出现的时候，就表示连通了
            reachable = sb.toString().contains("TTL");
            br.close();
            return reachable;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

}
