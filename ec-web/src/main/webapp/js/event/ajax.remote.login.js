define(function(require,exports,module) {
    var $ = require("jquery1.9");

    /**
     * 当EC用户登录完成
     */
    function onEcLogin() {
        $("#ec_head").off("load");
        $("#ec_head").on("load", function () {
            getLoginUser();
        });
    }

    /**
     * 获取当前登录用户
     */
    function getLoginUser() {
        $.ajax({
            type : "POST",
            url : Context.PATH +"/api/user/getloginuser?t="+Math.random(),
            success : function(data) {
                if(data.code == "5002"){
                    var user = data.data;
                    var html = '<div class="head-userlogin f-fl ">钢为欢迎你，<label id="gtxh_uame"><a href="' + Context.PATH + '/member" target="_blank">' + (user.name != "" ? user.name : user.mobile) + '</a></label><em>&nbsp;&nbsp;|&nbsp;&nbsp;</em><a id="loginOut" href="' + Context.PATH + '/logout">退出</a> </div>';
                    $(".head-usermenu").html(html);
                    $("#banner03 #userPhonetellogin").val(user.mobile);
                    $("#banner03 #userPhonetellogin").attr("readonly", "readonly");
                    $("#banner03 em#facas-phone").css("display", "none");
                    $("#banner03 #smsCodeNumberLi").css("display", "none");
                    $("#banner03 #word").html("我们将用0571-8971&nbsp;8799给您回电，请保持手机畅通。");
                    $("#banner05 #userPhonetellogin").val(user.mobile);
                    $("#banner05 #userPhonetellogin").attr("readonly", "readonly");
                    $("#banner05 em#facas-phone").css("display", "none");
                    $("#banner05 #smsCodeNumberLi").css("display", "none");
                    $("#banner05 #word").html("我们将用0571-8971&nbsp;8799给您回电，请保持手机畅通。");
                } else {
                    refeshHead();
                }
            },
            error : function(xhr, textStatus, errorThrown){
                refeshHead()
            }
        });
    }

    function refeshHead(){
        var loginUrl = Context.CAS_PATH+"/login?service="+ Context.ROOT+"/";
        var html = '<div class="head-userlogin f-fl">钢为欢迎你，<a rel="external nofollow" id="userregister" href="'+loginUrl+'">请登录</a><em>&nbsp;&nbsp;|&nbsp;&nbsp; </em><a rel="external nofollow" id="userregister" href="' + Context.PATH + '/user/register">免费注册</a> </div>'
        $(".head-usermenu").html(html);
    }

    /**
     * EC用户登录
     */
    exports.ecLogin  = function(){
        $('#min-header-ajax-login').html("<iframe id='ec_head' style='display: none' src='" + Context.PATH + "/remote-login'></iframe>");
        onEcLogin();
    }

});


