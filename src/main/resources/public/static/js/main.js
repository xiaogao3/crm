function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}

//退出登录方法
function logout() {
    //弹出消息框  标题  内容  方法体
    $.messager.confirm("来自crm","确定退出系统?",function (r) {
        if(r){
            //清除前台所有用户的信息
            $.removeCookie("userIdStr");
            $.removeCookie("userName");
            $.removeCookie("trueName");
            //设置延时提示信息
            $.messager.alert("来自crm","系统将在三秒后自动退出...","info");
            setTimeout(function () {
                //重定向访问index
                window.location.href=ctx+"/index";
            },3000);
        }
    })
}


function openPasswordModifyDialog() {
    //创建新的对话框
    $("#dlg").dialog("open").dialog("setTitle","密码修改");
}

function modifyPassword() {
    //指定表单的动作
    $("#fm").form("submit",{
        //对接后台的后缀
        url:ctx+"/user/updatePassword",
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data =JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm","密码修改成功,系统将在5秒后执行退出操作...","info");
                $.removeCookie("userIdStr");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                setTimeout(function () {
                    window.location.href=ctx+"/index";
                },5000)
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    })
}