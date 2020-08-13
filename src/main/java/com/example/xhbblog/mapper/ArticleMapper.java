package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArticleWithBLOBs record);

    int insertSelective(ArticleWithBLOBs record);

    List<ArticleWithBLOBs> selectByExampleWithBLOBs(ArticleExample example);

    List<Article> selectByExample(ArticleExample example);

    ArticleWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticleWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ArticleWithBLOBs record);

    int updateByPrimaryKey(Article record);


    List<ArticleWithBLOBs> findArticleLike(@Param("string") String string, @Param("published") Boolean published, @Param("order") Order order,@Param("uid") Integer uid);


    List<ArticleWithBLOBs> findAll(@Param("published") Boolean published,@Param("order") Order order,@Param("uid") Integer uid);


    List<ArticleWithBLOBs> findByTid(@Param("tid") Integer tid, @Param("published") Boolean published, @Param("order") Order order,@Param("uid") Integer uid);


    /**
     * 这里保证了一定不会删除其他用户的文章
     * @param uid
     * @param id
     */
    @Delete("DELETE FROM article WHERE id=#{id} AND uid=#{uid}")
    void deleteArticle(Integer id,Integer uid);

    @Update("UPDATE article SET visit =#{visit} where id=#{id}")
    void updateArticleVisit(Long visit, Integer id);

    @Update("UPDATE article SET bid =#{bid},top=false where id=#{aid}")
    void setArticleBanned(Integer aid,Integer bid);

    @Update("UPDATE article SET bid =null where id=#{aid}")
    void setArticleRelease(Integer aid);

    /**
     * 自动在置顶时解封
     * @param id
     */
    @Update("UPDATE ARTICLE SET top=true,published=true,bid=null WHERE id=#{id}")
    public void topArticle(Integer id);            //设为置顶(置顶时默认发表)

    @Update("UPDATE ARTICLE SET top=false WHERE id=#{id}")
    public void downArticle(Integer id);            //取消置顶

    @Select("SELECT COUNT(*) FROM article WHERE uid=#{uid}")
    public Integer countOfUid(Integer uid);

    @Select("SELECT COUNT(*) FROM article")
    public Integer count();

    /**
     * 用于导航栏
     * @param id
     * @return
     */
    @Select("SELECT id,title,uid FROM article WHERE id=#{id}")
    public Article getTitle(Integer id);

    @Select("select count(*) from article where tid=#{tid}")
    Integer countOfTag(Integer tid);        //同样用于后台不需要考虑published

    @Select("SELECT id,title FROM article WHERE id>#{aid} and published=true ORDER BY id limit 1")
    Article findNextArticle(Integer aid);

    @Select("SELECT id,title FROM article WHERE id<#{aid} and published=true ORDER BY id DESC limit 1")
    Article findLastArticle(Integer aid);


    @Select("select * FROM article WHERE id=#{id}")
    Article get(Integer id);

    @Select("select * from article where id=#{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),      //这里如果使用一个键值当作参数必须将其写在resultMap里去才能取出到对象里
            @Result(property = "lastArticle",column = "id",one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.findLastArticle")),
            @Result(property = "nextArticle",column = "id",one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.findNextArticle")),
    })
    ArticleWithBLOBs findById(Integer id);

    @Select("<script>" +
            "select * from article where 1=1" +
            "<when test='published!=null'>"+
            "AND published=#{published}"+
            "</when>" +
            "AND top=true" +
            "<when test='uid!=null'>" +
            "AND uid=#{uid}" +
            "</when>"+
            "order by id desc"
            +"</script>")
    List<ArticleWithBLOBs> findByTop(Boolean published,Integer uid);


    @Select("select id,visit,uid from article where published=true order by visit desc limit 3")     //最热门的访问文章
    List<Article> findForeArticle();

    @Select("select title,uid,id,tid,createTime,visit,firstPicture from article where published=true order by id desc limit 3")     //最新的三篇访问文章
    List<Article> findLastestArticle();      //用于博客页查询


    //这里发现mybatis一对多查询分页时如果要以那个"1"为标准进行分页必须要使用子查询

    @Select("select DISTINCT createTime from article where published=true order by createTime desc")
    @Results({
            @Result(property = "time",column = "createTime"),
            @Result(property = "articleList",column = "createTime",one=@One(select = "com.example.xhbblog.mapper.ArticleMapper.selectByCreateTime"))
    })
    List<TimeLine> findTimeLines();      //时间轴查询

    @Select("select id,title from article where published=true and createTime=#{createTime}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "title",column = "title")
    })
    List<Article> selectByCreateTime(Date createTime);
}