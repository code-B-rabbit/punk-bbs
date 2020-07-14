package com.example.xhbblog.Controller.admin;

import com.example.xhbblog.Service.ArticleService;
import com.example.xhbblog.Service.CommentService;
import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.annotation.NoRepeatSubmit;
import com.example.xhbblog.pojo.Comment;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.utils.PageInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CommentController {   //后台评论只有查找和删除逻辑,添加在前台进行完成

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    private Logger LOG= LoggerFactory.getLogger(CommentController.class);


    @ModelAttribute("msgCnt")
    public Long  msgs(@SessionAttribute("uid")Integer uid)
    {
        return userService.msgCnt(uid); //用户所获得的消息个数
    }

    @RequestMapping("/commentList")
    public String list(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "10")Integer count, Model model)
    {
        PageHelper.offsetPage(start,count);
        List<Comment> comments=commentService.list();
        model.addAttribute("page",new PageInfo<Comment>(comments));
        model.addAttribute("count",commentService.count());
        return "admin/commentList";
    }

    /**
     * 后台回复评论
     * @param comment
     * @return
     */
    @NoRepeatSubmit
    @RequestMapping(value = "/replyComment",method = RequestMethod.POST)//防止接口重复提交
    public @ResponseBody Object reply(@RequestBody Comment comment, HttpSession session) throws IOException {
        comment.setCreateTime(new Date());
        commentService.add(comment);   //这里是自己的回复所以不显示在websocket上了
        commentService.sendComment(comment,userService.get(comment.getUid()));
        return "success";
    }

    /**
     * 评论批量删除
     * @param cids
     */
    @RequestMapping(value = "/deleteComments",method = RequestMethod.POST)
    public @ResponseBody Object deleteCids(@RequestBody List<Integer> cids){
        Map<String,String> res=new HashMap<>();
        try{
            commentService.deleteCids(cids);
            res.put("msg","success");
        }catch (Exception e){
            res.put("msg","请检查批量删除的评论项中是否有评论含有未删除子评论");
        }
        return res;
    }


    /**
     * 根据属性值判断跳转地址
     * @param id
     * @param aid
     * @param uid
     * @return
     */
    @RequestMapping("/deleteComment")
    public String delete(Integer id,Integer aid,Integer uid)
    {
        LOG.info("删除评论ID:{}",id);
        commentService.delete(id);
        if(aid!=null){
            return "redirect:/admin/listByAid?aid="+aid;
        }else if(uid!=null){
            return "redirect:/admin/listByUid?uid="+uid;
        }
        return "redirect:/admin/commentList";
    }

    @RequestMapping("/listByAid")
    public String listByAid(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "10")Integer count, Integer aid, Model model,
                            HttpServletRequest request)
    {
        PageHelper.offsetPage(start,count);
        List<Comment> comments=commentService.listByAid(aid);
        model.addAttribute("page",new PageInfo<Comment>(comments));
        model.addAttribute("article",articleService.getTitle(aid));
       // model.addAttribute("count",commentService.countOfComment(aid));
        model.addAttribute("limit","aid="+aid);
        return "admin/commentList";
    }

    /**
     * 查询某用户下的全部评论
     * @param start
     * @param count
     * @param uid
     * @param model
     * @return
     */
    @RequestMapping("/listByUid")
    public String listByUid(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "10")Integer count,Integer uid,Model model)
    {
        //Integer total=commentService.countOfUser(uid);
        PageHelper.offsetPage(start,count);
        List<Comment> comments=commentService.listByUid(uid);
        model.addAttribute("page",new PageInfo<Comment>(comments));
        //model.addAttribute("count",total);
        model.addAttribute("limit","uid="+uid);
        model.addAttribute("user",userService.get(uid));
        return "admin/commentList";
    }

    /**
     * 某评论的全部回复
     */
    @RequestMapping("/listByCid")
    public String listByCid(@RequestParam(name = "start",defaultValue = "0")Integer start, @RequestParam(name = "count",defaultValue = "10")Integer count,Integer cid,Model model)
    {
       // Integer total=commentService.countOfPar(cid);
        PageHelper.offsetPage(start,count);
        List<Comment> comments=commentService.findChilds(cid);
        model.addAttribute("page",new PageInfo<Comment>(comments));
        //model.addAttribute("count",total);
        model.addAttribute("limit","cid="+cid);
        model.addAttribute("cmt",commentService.get(cid));
        return "admin/commentList";
    }
}
