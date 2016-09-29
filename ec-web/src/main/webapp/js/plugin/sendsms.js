define(function(require,exports,module){
    var $ = require("jquery"),
    	util = require("util"),
        tips = require("checkformtips"),
        sendsmsCallback = require("sendsmscallback"),clearInt,code,validCode = true;
        exports.sendSms = function(url,phone){
            $(".sms_btn").on('click', function () {
            	var phone = $("#userPhonetellogin").val();

                if ($(this).hasClass("msg")){
                    var obj = {
                        elm : $("#userPhonetellogin"),
                        type : 1,
                        errortip :2
                    }
                    tips.checkFormTips(obj,"");
                    return false;
                };
                if($.trim(phone) == "") {
                    var obj = {
                        elm : $("#userPhonetellogin"),
                        type : 1,
                        errortip :1
                    }
                    tips.checkFormTips(obj,"手机号码不能为空");
                    return false;
                }else if(!(/^1[3|4|5|7|8][0-9]\d{4,8}$/.test(phone))) {
                    var obj = {
                        elm : $("#userPhonetellogin"),
                        type : 1,
                        errortip :1
                    }
                    tips.checkFormTips(obj,"手机号码格式不正确");
                    return false;
                }else{
                    var vcode =$("#vcode").val();
                    if(vcode !=undefined || $('#vcode').size() >0){
                        var obj = {
                        elm : $("#vcode"),
                        type : 1,
                        errortip :1
                        }
                        if($.trim(vcode) == ""){
                            tips.checkFormTips(obj,"验证码错误");
                            return false;
                        }else if($.trim(vcode).length!=4){
                            tips.checkFormTips(obj,"验证码错误");
                            return false;
                        }
                    }
                    var time=60;
                    code=$(this);
                    if (validCode) {
                        validCode=false;
                        code.text("60秒");
                        code.addClass("msg");
                        code.addClass("msgnum");
                         clearInt=setInterval(function  () {
                            time--;
                            code.html(time+"秒");
                            if (time==0) {
                                clearInterval(clearInt);
                                code.html("重新获取");
                                validCode=true;
                                code.removeClass("msg");
                            }
                        },1000)
                    }
                    $("#smsCodeNumber").closest("li").find(".form-err").hide();
                    smsSend(phone,url,vcode); //Ajax发送给短信服务端

                }
            })
        }
    function smsSend(phone,url,vcode){
        var o = {
			url : url,
			data: {"phone":phone,vcode:vcode},
			success:function(data){
                if(data.code == "15002"){
                    clearInterval(clearInt);
                    code.html("获取验证码");
                    validCode=true;
                    code.removeClass("msg");
                }
				sendsmsCallback.sendsmsCallback(data);
			}
		}
		util.ajax({config:o});
    }
    
})
