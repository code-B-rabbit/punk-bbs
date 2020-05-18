function scroll() {
    if(window.pageXOffset!==null)//新型模式
    {
        return {
            top:window.pageYOffset,
            left:window.pageXOffset
        }
    }else if(document.compatMode==="CSS1Compat"){//严格模式
        return{
            top:document.documentElement.scrollTop,
            left:document.documentElement.scrollLeft,
        }
    }
    return{//异常模式
        top:document.body.scrollTop,
        left:document.body.scrollLeft,
    }
}

window.onload = function () {
    var OffsetHeight=document.getElementById("header").offsetTop;
    //计算导航栏距离顶部的距离
    console.log(OffsetHeight);
    var nav1=document.getElementById("header");
    //获取nav
    console.log(nav1);
    //2、监听窗口的滚动
    window.onscroll = function () {
        var scrolltop=scroll().top;//获取滚动条的滚动长度
        console.log(scrolltop);
        if(scrolltop>=OffsetHeight){
            nav1.className="header";
        }else
            nav1.className=" ";
    }
}