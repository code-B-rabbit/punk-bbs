$(
    function () {

        isEmpty=function(str)
        {
            return str==null||str==""||str.trim().length==0;
        };

        checkName=function () {
            nm=$("[name='name']").val();
            var anw=true;
            if(isEmpty(nm))
            {
                $('div#name').show();
                $('div#name').text("姓名不能为空");
                anw=false;
            }else if(nm.trim().length!=nm.length)
            {
                $('div#name').show();
                $('div#name').text("名字中不能含有空格");
                anw=false;
            }
            else
            {
                $.ajax({
                    //请求方式
                    type : "GET",
                    //请求的媒体类型
                    contentType: "application/json;charset=UTF-8",
                    //请求地址
                    url : "/user/check?name="+nm,
                    //数据，json字符串
                    async : false,        //不设置异步
                    //请求成功
                    success : function(result) {
                        if(result.exists==true)
                        {
                            $('div#name').show();
                            $('div#name').text("姓名不能重复");
                            anw=false;
                        }else{
                            $('div#name').hide();
                            anw=true;
                        }
                    }
                })
            };
            return anw;
        };

        $("[name='name']").blur(
            function()
            {
                checkName();
            }
        );


        checkEmail=function () {
            email=$("[name='email']").val();
            var reg = /^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\.[a-z]{2,}$/;
            var anw=false;
            if(isEmpty(email))
            {
                $('div#email').text("邮箱不能为空");
                $('div#email').show();
            }else if(reg.test(email)===-1||email.indexOf('@')===-1)
            {
                $('div#email').text("邮箱格式错误");
                $('div#email').show();
            }else
            {
                $.ajax({
                    //请求方式
                    type : "GET",
                    //请求的媒体类型
                    contentType: "application/json;charset=UTF-8",
                    //请求地址
                    url : "/user/checkEmail?email="+email,
                    //数据，json字符串
                    async : false,        //不设置异步
                    //请求成功
                    success : function(result) {
                        if(result.exist==true)
                        {
                            $('div#email').text("邮箱地址不能重复");
                            $('div#email').show();
                        }else if(result.exist==false){
                            anw=true;
                            $('div#email').hide();
                        }else{
                            $('div#email').text("服务器繁忙,请等待稍后再试");
                            $('div#email').show();
                        }
                    }
                });
            };
            return anw;
        };

        $("[name='email']").blur(
            function () {
                checkEmail();
            }
        );

        checkPassword=function () {
            password=$("[name='password']").val();
            var anw=false;
            if(isEmpty(password))
            {
                $('div#password').show();
                $('div#password').text("密码不能为空或全空格");
            }else if(password.trim().length!=password.length)
            {
                $('div#password').show();
                $('div#password').text("密码中不得出现非法字符如空格");
            }else if(password.length>12||password.length<6)
            {
                $('div#password').show();
                $('div#password').text("密码规定六位至十二位");
            }else {
                $('div#password').hide();
                anw = true;
            }
            return anw;
        };

        $("[name='password']").blur(
            function () {
                checkPassword();
            }
        );

        passWordEqual=function () {
            return $("[name='password']").val()===$("[name='passwordCheck']").val()
        };

        $("[name='passwordCheck']").blur(
            function () {
                if(!passWordEqual())
                {
                    $('#passwordCheck').show();
                }else
                {
                    $('#passwordCheck').hide();
                }
            }
        );
        
        $("form").submit(
            function (event) {
                if(checkName()&&checkEmail()&&checkPassword()&&passWordEqual())
                {
                    alert("注册成功！,正在返回首页");
                    window.history.go(-2);
                }else
                {
                    alert("请检查您的注册信息");
                    event.preventDefault();
                }
            }
        );
    }
)