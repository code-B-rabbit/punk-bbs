package com.example.xhbblog.Controller.admin;

import com.alibaba.fastjson.support.odps.udf.CodecCheck;
import com.example.xhbblog.Service.ArticleService;
import com.example.xhbblog.Service.TagService;
import com.example.xhbblog.Service.ThumbsService;
import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.Service.impl.ArticleServiceImpl;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ArticleController {


    private Logger LOG=LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ArticleService articleService;

    @Autowired
    public TagService tagService;

    @Autowired
    public UserService userService;

    @Autowired
    public ThumbsService thumbsService;

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
    @RequestMapping("/articleList")
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
    @RequestMapping("/articleListByTag")
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
    @RequestMapping("/articleLike")
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
       // model.addAttribute("count",pageInfo.getTotal());
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

    @RequestMapping("/addArticle")
    public String add(Model model)
    {
        model.addAttribute("tags",tagService.list());
        return "admin/articleAdd";
    }
    //增加

    @RequestMapping("/editArticle")
    public String edit(Integer id,Model model)
    {
        model.addAttribute("tags",tagService.list());
        model.addAttribute("article",articleService.findById(id,"dddddd"));
        return "admin/articleEdit";
    }
    //修改

    //这里修改和新增保存在了一个方法里
    @RequestMapping("/articleSave")
    public String save(ArticleWithBLOBs article)
    {
        if(article.getId()==null)         //新增
        {
            article.setCreateTime(new Date());
            articleService.add(article);
        }else
        {
            articleService.update(article);
        }
        return "redirect:/admin/articleList";
    }

    @RequestMapping("/deleteArticle")
    public String delete(Integer id,Integer tid,Integer uid,Integer order,Boolean published,String key)
    {
        articleService.deleteArticle(id,uid);
        String url="redirect:/admin/articleList?";   //默认查所有
        if(tid!=null){               //按标签查
            url="redirect:/admin/articleListByTag?tid="+tid;  //路由控制
        }
        if(key!=null){
            url="redirect:/admin/articleLike?key="+key;
        }
        if(order!=null){
            url+="&order="+order;
        }
        if(published!=null){
            url+="&published="+published;
        }
        if(uid!=null){       //是否用用户uid约束
            url+="&uid="+uid;
        }
        return url;
    }

    @RequestMapping("/getArticle")
    public String get(Integer id)
    {
        return "redirect:/article?id="+id; //重定向到fore路径下
    }

    /**
     * 将博客内容置顶
     * @param id
     * @return
     */
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
}
