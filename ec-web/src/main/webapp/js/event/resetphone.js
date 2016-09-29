define(function(require,exports,module){
    var $ = require("jquery"),
        tips = require("checkformtips"),
        util = require("util"),
        cache=require("cache"),
        constant = require("constant"),
        prcsteel=require("prcsteel"),
        sendsms = require("sendsms");
       
    /* 获得焦点清空输入框 */
    $("#userPhonetellogin").inputFocus();
    $("#smsCodeNumber").inputFocus();
    $("#oldCode").inputFocus();

    /* 验证表单 */
    $("#oldcheck").verifyForm(2,blur);
    $("#newphone").verifyForm(2,blur);
    
    /* 点击清空按钮清空表单内容 */
    $(".input-empty").click(function(){
        $(this).closest(".item-text").find("input").val("");
    })
    
    //qq客服
    $(".qqlink").bind("click",function(){
    	util.setqq();
    })

    /* 失去焦点验证手机号码 */
    $("#userPhonetellogin").change(function(){ 	
        var val = $(this).val(),obj = {};
        if(val == ""){
            var obj = {
                elm : $("#userPhonetellogin"),
                type : 1,
                errortip :1
            }
            tips.checkFormTips(obj,"手机号码不能为空");
            $(".sms_btn").addClass("msg");
            return false;
        }else if(!(/^1[3|4|5|7|8][0-9]\d{8}$/.test(val))) {
            var obj = {
                elm : $("#userPhonetellogin"),
                type : 1,
                errortip :1
            }
            tips.checkFormTips(obj,"手机号码格式不正确");
            $(".sms_btn").addClass("msg");
            $(".sms_btn").css("enable","false")
            return false;
        }else{
            var obj = {
                elm : $("#userPhonetellogin"),
                type : 1,
                errortip :2
            }
            tips.checkFormTips(obj,"");
            checkNewPhone(val);
            $(".sms_btn").removeClass("msg");
            $(".sms_btn").css("enable","true")
            
        }
    })

    /* 确认密码 */
    $("#Stepgo").click(function(){
        var oldCode = $("#oldCode").val()
        if(oldCode == "" || !(/^\d{4}$/.test(oldCode))){
            var obj = {
                elm : $("#oldCode"),
                type : 1,
                errortip :1
            }
            tips.checkFormTips(obj,"验证码错误");
            return false;
        }
        checkOldCode(oldCode);
    });

   //校验验证码是否为空
   var checkCodeNotempty = function(){
        var smsCodeNumber = $("#smsCodeNumber").val(),isTrue =  checkUserPhoneNotempty($("#userPhonetellogin"));
        if(!isTrue){
        	return;
        };
        if(smsCodeNumber == "" || !(/^\d{4}$/.test(smsCodeNumber))){
            var obj = {
                elm : $("#smsCodeNumber"),
                type : 1,
                errortip :1
            }
            tips.checkFormTips(obj,"验证码错误");
            return false;
        }
        return true;
   };
    
   //点击绑定按钮校验表单是否为空，绑定短信发送
   $("#bindNewPhone").click(function(){
        var smsCodeNumber = $("#smsCodeNumber").val(),
        userPhonetellogin = $("#userPhonetellogin").val(),
        isTrue =  checkUserPhoneNotempty($("#userPhonetellogin")),isCodeTrue = checkCodeNotempty();  
        if(!isTrue || !isCodeTrue){
        	return false;
        };
       
        confirmBindPhone(userPhonetellogin,smsCodeNumber);//确定绑定     
    })

	//点击发送短信校验手机号码是否为空
   $("#getNewCode").click(function(){
        var userPhonetellogin = $("#userPhonetellogin").val(),
        isTrue =  checkUserPhoneNotempty($("#userPhonetellogin"));        
        if (!isTrue){
        	return;
        }
   })
   
    //返回首页
    $("#gobackindex").click(function(){
	    var config = {
        	type: "POST",
        	dataType: "json",
			url : "/ajaxlogout",
			success:function(data){
				if(data.code == "0"){
					window.location.href = cache.base_url+"/";
				}
			}
		}
		util.ajax({config:config});
    })
    
   	var showstep = function(num){
        $(".showstep").addClass("hide");
        $("#step"+num).removeClass("hide");
    }
 
	// 	校验新验证码
    var checkNewCode = function(){
    	var config = {
			url : "/api/user/phone/code/check",
			dataType: "json",
			success:function(data){
				ajaxCallback.loginCheckCallback(data);
			}
		}
		util.ajax({config:config});
    }
    
    //校验新手机号码是否存在
    var checkNewPhone = function(phone){
        var config = {
			url : "/api/user/phone/check",
			dataType: "json",
			data:{"phone":phone},
			success:function(data){
				 ajaxCallback.checkNewPhoneCallback(data,function(){sendsms.sendSms("/api/code/send/resetphone_new",phone)});
			}
		}
		util.ajax({config:config});

    }

	//校验旧验证码是否正确
    var checkOldCode = function(data){
        var config = {
        	type: "POST",
			url : "/api/user/phone/code/check",
			dataType: "json",
			data:{"code":data},
			success:function(data){
				 ajaxCallback.checkOldCodeCallback(data);
			}
		}
		util.ajax({config:config});

    }
    
    //10秒倒计时
    var setIntervalTimeOut = function(){
    	var time = 5;
	    var code = $(".reg-msg span");
	    var t=setInterval(function() {
	          time--;
	          code.html(time);
	          if (time==0) {
	              clearInterval(t);
	              window.location.href = cache.base_url+"/logout";
	          }
	        },1000);
    }
    
	//验证旧手机号
    var resetPhonesendSms = function(url,phone){
            var validCode;
            $("#getCode").click (function  () {
            	if($(this).hasClass("msg")){
		    		validCode=false;
		    		return;
		    	}
		    	validCode=true;
                var time=60;
                var code=$(this);
                if (validCode) {
                    validCode=false;
                    code.text("60秒");
                    code.addClass("msg");
                    code.addClass("msgnum");
                    var t=setInterval(function  () {
                        time--;
                        code.html(time+"秒");
                        if (time==0) {
                            clearInterval(t);
                            code.html("重新获取");
                            validCode=true;
                            code.removeClass("msg");
                        }
                    },1000)
                }
                    smsSend(); //Ajax发送给短信服务端

            })
        }
	//确定绑定新手机
	var confirmBindPhone = function(phone,code){
        var config = {
        	type: "POST",
			url : "/api/user/phone/resetphone",
			dataType: "json",
			data:{"newPhone":phone,"newCode":code},
			success:function(data){
				ajaxCallback.confirmBindPhoneCallback(data);
			}
		}
		util.ajax({config:config});
	}
	
	
	//发送短信
    var smsSend = function(){ 
        var config = {
        	type: "POST",
			url : "/api/code/send/resetphone_old",
			dataType: "json",
			success:function(data){
				ajaxCallback.sendsmsCallback(data);
			}
		}
		util.ajax({config:config});
    }
    
   	//校验手机号码是否为空
	var checkUserPhoneNotempty = function(elmid){
		var val = elmid.val();
		if(val == ""){
			var obj = {
				elm : elmid,
				type : 1,
				errortip :1
			}
			tips.checkFormTips(obj,"手机号码不能为空");
			elmid.focus();
			return false;
		}else if(!(/^1[3|4|5|7|8][0-9]\d{8}$/.test(val))) {
			var obj = {
				elm : elmid,
				type : 1,
				errortip :1
			}
			tips.checkFormTips(obj,"手机号码格式不正确");
			elmid.focus();
			return false;
		}
		return true;
	};

   
   //回调函数
   var ajaxCallback = {
	   	checkOldCodeCallback:function(data){
	        var obj;
	        if(data.code !=2007){
	        	obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,constant.getCodeMsg(data.code));
	            return false;
	        }
	        showstep(2);
            obj = {
                elm : $("#oldCode"),
                type : 1,
                errortip :1
            }
            tips.checkFormTips(obj,"");
	    },
	    sendsmsCallback:function(data){
	      var obj;
	      	if(data.code !=2000){
	        	obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,constant.getCodeMsg(data.code));
	            return false;
	        }
	        obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :2
	            }
	            tips.checkFormTips(obj,constant.getCodeMsg(data.code));
	    },
	    
	//    检查新手机号码是否存在
		checkNewPhoneCallback:function(data,fun){
			var obj = {},userPhonetellogin = $("#userPhonetellogin").val();
				if(data.code ==1002){
					obj = {
						elm : $("#userPhonetellogin"),
						type : 1,
						errortip :1
					}
					tips.checkFormTips(obj,"手机号已存在");
					$(".sms_btn").addClass("msg");
					return false;
				}else if(data.code ==1004){
					obj = {
						elm : $("#userPhonetellogin"),
						type : 1,
						errortip :2
					}
					tips.checkFormTips(obj,"手机号码可用");
					if (!$(".sms_btn").hasClass("msgnum")) {
						$(".sms_btn").removeClass("msg");
					}
	
					fun();
				}else if(data.code ==1003){
					obj = {
						elm : $("#userPhonetellogin"),
						type : 1,
						errortip :1
					}
					tips.checkFormTips(obj,"手机号码不正确");
					$(".sms_btn").addClass("msg");
					return false;
				}
		},
		//确定绑定手机号码 
		confirmBindPhoneCallback:function(data){
			var obj = {},userPhonetellogin = $("#userPhonetellogin").val();
		
			if(data.code ==1006){
				obj = {
					elm : $("#userPhonetellogin"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"手机号码修改失败");
				$(".sms_btn").addClass("msg");
				return false;
			}else if(data.code ==1007){
	            obj = {
	                elm : $("#userPhonetellogin"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"手机号码修改异常");
	            return false;
	        }else if(data.code ==2001){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码发送失败");
	            return false;
	        }else if(data.code ==2002){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else if(data.code ==2003){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码发送太频繁");
	            return false;
	        }else if(data.code ==2004){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else if(data.code ==2008){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else if(data.code ==1005){
	            obj = {
	                elm : $("#userPhonetellogin"),
	                type : 1,
	                errortip :2
	            }
	            tips.checkFormTips(obj,"");
	            showstep(3);
	            setIntervalTimeOut();
	            
	        }
		}
   }
    /* 绑定发送短信事件和显示第一区块*/	
    resetPhonesendSms();
    showstep(1);

})