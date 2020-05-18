package com.example.xhbblog.mapper;

import com.example.xhbblog.pojo.Article;
import com.example.xhbblog.pojo.ArticleExample;
import com.example.xhbblog.pojo.ArticleWithBLOBs;
import com.example.xhbblog.pojo.TimeLine;
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


    @Select("select * from article where id=#{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),      //这里如果使用一个键值当作参数必须将其写在resultMap里去才能取出到对象里
            @Result(property = "tag", column = "tid", one = @One(select = "com.example.xhbblog.mapper.TagMapper.selectByPrimaryKey")),
            @Result(property = "nextName",column = "id",one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.findNextName")),
            @Result(property = "nextId",column = "id",one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.findNextId")),
            @Result(property = "lastName",column = "id",one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.findLastName")),
            @Result(property = "lastId",column = "id",one = @One(select = "com.example.xhbblog.mapper.ArticleMapper.findLastId")),
            @Result(property = "commentSize",column = "id",one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment")),
    })
    ArticleWithBLOBs findById(Integer id);

    @Select("<script>" +
            "select * from article" +
            "<if test='published!=null'>"+
            "where published=#{published} " +
            "</if>"+
            "order by id desc" +
            "</script>")           //在注解中使用published动态拼接
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),
            @Result(property = "tag",column = "tid",one = @One(select = "com.example.xhbblog.mapper.TagMapper.selectByPrimaryKey")),
            @Result(property = "commentSize",column = "id",one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment")),
    }
    )
    List<Article> listAll(Boolean published);        //给后台用的


    @Select("<script>" +
            "select * from article where tid=#{tid}" +
            "<when test='published!=null'>"+
            "and published=#{published}"+
            "</when>"+
            "order by id desc" +
            "</script>")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),
            @Result(property = "tag", column = "tid", one = @One(select = "com.example.xhbblog.mapper.TagMapper.selectByPrimaryKey")),
            @Result(property = "commentSize", column = "id", one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment"))
    })
    List<Article> listByTid(Integer tid, Boolean published);       //用于后台


    @Select("<script>" +
            "select * from article where id IN(select id from article where info like #{string} or title like #{string})" +
            "<when test='published!=null'>"+
            "and published=#{published}"+
            "</when>"+
            "order by id desc"+
            "</script>")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),
            @Result(property = "tag", column = "tid", one = @One(select = "com.example.xhbblog.mapper.TagMapper.selectByPrimaryKey")),
            @Result(property = "commentSize", column = "id", one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment"))
    })
    List<Article> listArticleLike(String string, Boolean published);         //给后台用


    @Select("select * from article where published=true and id IN (select id from article where info like #{string} or title like #{string}) " +
            "order by id desc")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),
            @Result(property = "tag", column = "tid", one = @One(select = "com.example.xhbblog.mapper.TagMapper.selectByPrimaryKey")),
            @Result(property = "commentSize", column = "id", one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment"))
    })
    List<ArticleWithBLOBs> findArticleLike(String string);

    @Select("select * from article where published=true order by id desc")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),
            @Result(property = "tag",column = "tid",one = @One(select = "com.example.xhbblog.mapper.TagMapper.selectByPrimaryKey")),
            @Result(property = "commentSize",column = "id",one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment")),
    }
    )
    List<ArticleWithBLOBs> findAll();



    @Select("select * from article where tid=#{tid} and published=true order by id desc")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),
            @Result(property = "tag", column = "tid", one = @One(select = "com.example.xhbblog.mapper.TagMapper.selectByPrimaryKey")),
            @Result(property = "commentSize", column = "id", one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment"))
    })
    List<ArticleWithBLOBs> findByTid(Integer tid);



    @Select("select count(*) from article where tid=#{tid}")
    Integer countOfTag(Integer tid);        //同样用于后台不需要考虑published

    @Select("select id,visit,title,firstPicture from article where published=true order by visit desc limit 3")     //最热门的访问文章
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "commentSize", column = "id", one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment"))
    })
    List<Article> findForeArticle();

    @Select("select title from article where id<#{aid} and published=true order by id desc limit 1")
    String findLastName(Integer aid);

    @Select("select id from article where id<#{aid} and published=true order by id desc limit 1")      //这样按时间顺序排是对的吗....
    Integer findLastId(Integer aid);

    @Select("select title from article where id>#{aid} and published=true order by id limit 1")
    String findNextName(Integer aid);

    @Select("select id from article where id>#{aid} and published=true order by id limit 1")
    Integer findNextId(Integer aid);

    @Select("select * from article where published=true order by id desc limit 3")     //最新的三篇访问文章
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),
            @Result(property = "tag", column = "tid", one = @One(select = "com.example.xhbblog.mapper.TagMapper.selectByPrimaryKey")),
            @Result(property = "commentSize", column = "id", one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment")),
            @Result(property = "createTime",column = "createTime"),
    })
    List<Article> getLastestArticle();    //用于首页查询

    @Select("select title,id,tid,createTime,visit,firstPicture from article where published=true order by id desc limit 3")     //最新的三篇访问文章
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "tid", column = "tid"),
            @Result(property = "title",column = "title"),
            @Result(property = "tag", column = "tid", one = @One(select = "com.example.xhbblog.mapper.TagMapper.selectByPrimaryKey")),
            @Result(property = "commentSize", column = "id", one = @One(select = "com.example.xhbblog.mapper.CommentMapper.countOfComment")),
            @Result(property = "createTime",column = "createTime"),
            @Result(property = "visit",column = "visit"),
            @Result(property = "firstPicture",column = "firstPicture"),
    })
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