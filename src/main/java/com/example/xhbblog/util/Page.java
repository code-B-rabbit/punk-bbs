package com.example.xhbblog.util;
//用于分页的对象
public class Page {

    private Integer start=0;        //开始(默认为0)
    private Integer count=5;        //每页含多少元素(默认为5)
    private Integer total;         //一共有多少元素
    private String param=""; //参数


    public static Integer ADMIN_COUNT=5;
    public static Integer PRODUCTS_OF_CATEGORY=12;      //每页参数的常量配置

    //用于判断
    public Integer getLast()        //获得最后一页开始页
    {
        if(total%count==0)
        {
            return total-count<0?0:total-count;
        }else
        {
            return total-(total%count)<0?0:total-(total%count);
        }
    }

    @Override
    public String toString() {
        return "Page{" +
                "start=" + start +
                ", count=" + count +
                ", total=" + total +
                ", param='" + param + '\'' +
                '}';
    }

    public boolean hasNext()
    {
        return this.start!=this.getLast();
    }

    public boolean hasPreviouse()
    {
        return this.start!=0;
    }

    public Integer getTotalPage()
    {
        if(total%count==0)
        {
            return total/count;
        }else
        {
            return total/count+1;
        }
    }



    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
