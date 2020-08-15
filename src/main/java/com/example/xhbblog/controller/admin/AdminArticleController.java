package com.example.xhbblog.controller.admin;

import com.example.xhbblog.pojo.BannedInfo;
import com.example.xhbblog.service.*;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminArticleController {


    private Logger LOG=LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ArticleService articleService;

    @Autowired
    public TagService tagService;

    @Autowired
    public UserService userService;

    @Autowired
    public ThumbsService thumbsService;

    @Autowired
    private ArticleBannedService articleBannedService;

    @Autowired
    private BannedInfoService bannedInfoService;

    @ModelAttribute("bannedInfos")
    public List<BannedInfo> listBannedInfos(){
        return bannedInfoService.findAll();
    }

    @ModelAttribute("msgCnt")
    public Long  msgs(HttpSession session)
    {
        Integer uid= (Integer) session.getAttribute("uid");
        if(uid!=null){
            return userService.msgCnt(uid); //用户所获得的消息个数
        }else{
            return null;
        }
    }

    /**
     * 按照是否发表,以及按字段排序查找全部
     * 已发表,或者全部文章,则查找置顶的博客项
     * 以及查找某个人的文章列表(同时也查询一下这个人被置顶的文章是什么)
     * 按标签查,按用户查,按搜索查
     * @param start
     * @param count
     * @param model
     * @param published
     * @param order
     * @return
     */
    @GetMapping("/articleList")
    private String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count,Boolean published
    ,@RequestParam(name = "order",defaultValue = "0") Integer order,Integer uid, Model model)
    {
        if(Math.abs(order)==3){
            thumbsService.redisDataToMySQL();
        }
        if(start==0&&(order==null||order==0)&&(published==null||published==true))               //未发表则不查找置顶项(置顶必发表)
        {
           model.addAttribute("tops",articleService.topArts("....",true,uid));
        }
        PageHelper.offsetPage(start,count);
        List<ArticleWithBLOBs> articles=articleService.listAll(published,order,uid);
        model.addAttribute("order",order);
        model.addAttribute("page",new PageInfo<ArticleWithBLOBs>(articles));
        String limit="";
        if(published!=null)
        {
            limit="published="+published+"&order="+order;
        }else{
            limit="&order="+order;
        }
        if(uid!=null){
            limit+="&uid="+uid;
        }
        LOG.info("uid为:{}",uid);
        LOG.info("限制值为:{}",limit);
        model.addAttribute("limit",limit);
        if(uid!=null){
            model.addAttribute("user",userService.get(uid));
        }
        return "admin/articleList";
    }

    /**
     * 按照是否发表,以及按字段排序查找某标签下的全部博文
     * @param start
     * @param count
     * @param model
     * @param published
     * @param order
     * @return
     */
    @GetMapping("/articleListByTag")
    private String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model,Integer tid,Boolean published
    ,@RequestParam(name = "order",defaultValue = "0") Integer order,Integer uid)
    {
        if(Math.abs(order)==3){
            thumbsService.redisDataToMySQL();
        }
        model.addAttribute("tag",tagService.get(tid));
        model.addAttribute("order",order);
        PageHelper.offsetPage(start,count);
        List<ArticleWithBLOBs> articles=articleService.listByTid(tid,published,order,uid);   //后台调用标签的管理方法
        model.addAttribute("page",new PageInfo<ArticleWithBLOBs>(articles));
        String limit="tid="+tid;
        if(published!=null)
        {
            limit+="&published="+published;            //添加分页的约束条件
        }
        if(order!=null){
            limit+="&order="+order;
        }
        LOG.info("限制值为:{}",limit);
        model.addAttribute("limit",limit);
        return "admin/articleList";
    }

    /**
     * 按照是否发表,以及按字段排序查找模糊搜索下的全部博文
     * @param start
     * @param count
     * @param model
     * @param published
     * @param order
     * @return
     */
    @GetMapping("/articleLike")
    public String research(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "5")Integer count, Model model,String key
    ,Boolean published,@RequestParam(name = "order",defaultValue = "0") Integer order,Integer uid)
    {
        if(Math.abs(order)==3){
            thumbsService.redisDataToMySQL();
        }
        PageHelper.offsetPage(start,count);
        List<ArticleWithBLOBs> articles=articleService.listArticleLike(key,published,order,uid);
        model.addAttribute("order",order);
        PageInfo<ArticleWithBLOBs> pageInfo=new PageInfo<ArticleWithBLOBs>(articles);
        model.addAttribute("page",pageInfo);
        String limit="key="+key;
        if(published!=null)
        {
            limit+="&published="+published;            //添加分页的约束条件
        }
        if(order!=null){
            limit+="&order="+order;
        }
        model.addAttribute("limit",limit);
        return "admin/articleList";
    }

    @RequestMapping("/TopArticle")
    public String topArticle(Integer id)
    {
        articleService.topArticle(id);
        return "redirect:/admin/articleList";
    }

    /**
     * 将博客内容取消置顶
     * @param id
     * @return
     */
    @RequestMapping("/DownArticle")
    public String downArticle(Integer id)
    {
        articleService.downArticle(id);
        return "redirect:/admin/articleList";
    }


    /**
     * 封禁某篇文章
     * @param bid
     * @param aid
     */
    @PostMapping("/bannedArticle")
    public @ResponseBody void banned(@RequestParam(value = "bid",required = true)Integer bid,@RequestParam(value = "aid",required = true)Integer aid) throws IOException {
        articleBannedService.bannedArticle(bid,aid);
    }

    /**
     * 解封文章
     * @param aid
     */
    @GetMapping("/releaseArticle/{aid}")
    public @ResponseBody void release(@PathVariable("aid")Integer aid) throws IOException {
        articleBannedService.relieveArticle(aid);
    }

}
