package com.example.xhbblog.pojo;

/**
 * 用于文章类排序的类
 */
public class Order {
    public String by;     //
    public String direct;

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    @Override
    public String toString() {
        return "Order{" +
                "by='" + by + '\'' +
                ", direct='" + direct + '\'' +
                '}';
    }

    public static Order getOrder(Integer rank)
    {
        if(rank==null||rank==0){
            return null;
        }
        Order o=new Order();
        switch (Math.abs(rank)){
            case 1:
                o.setBy("visit");
                break;
            case 2:
                o.setBy("comment");
                break;
            case 3:
                o.setBy("thumb");
                break;
        }
        if(rank<0){
            o.setDirect("DESC");   //递减
        }else{
            o.setDirect("");   //递增
        }
        return o;
    }

}
