define(function(require,exports,module){
    var $ = require("jquery"),
        util = require("util"),
        caslogin = require("caslogin"),
        cache=require("cache"),
        prcsteel=require("prcsteel"),
		userCallback = require("usercallback"),
        tips = require("checkformtips"),
        sendsms = require("sendsms");
    var regitser = {
    	
    	init:function(){
    		 /* 获得焦点清空输入框 */
		    $("#userPhone").inputFocus();
		    $("#userPwd").inputFocus();
			$("#vcode").inputFocus();
		    $("#afterPwd").inputFocus();
		    $("#userPhonetellogin").inputFocus();
		    $("#smsCodeNumber").inputFocus();

		    /* 验证表单 */
		    $("#userRegister").verifyForm(2,blur);
		
		
		    /* 点击清空按钮清空表单内容 */
		    $(".input-empty").click(function(){
		        $(this).closest(".item-text").find("input").val("");
		    })

			$('#codeImage').click(function(){
				regitser.chageCode();
			});
		
		    // 按钮
		    $("#readnow").click(function(){
		        if ($(this).is(":checked")) {
		             $("#submit").css({"background-color":"#c81623"}).removeAttr("disabled");
		        } else {
		            $("#submit").css({"background-color":"#928889"}).attr("disabled","disabled");
		        }
		    })
		
		    /* 协议弹出 */
		    $(".readnow a").click(function(){
		        $(".mcover").show();
		        $(".pact_con_bg").slideDown();
		    })
		    /* 协议关闭 */
		    $(".closewindow").click(function(){
		    	if($(this).hasClass("goto-btn")){
		    		if(!$("#readnow").is(":checked")){
		    			$("#readnow").click();
		    		}
		    		
		    	}
		        $(".mcover").hide();
		        $(".pact_con_bg").slideUp();
		    })
		
		    /* 验证手机号码是否存在 */
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
		        var config = {
					url : "/api/user/phone/check",
					data:{"phone":val},
					success:function(data){
						userCallback.checkUserRegCallback(data,function(){sendsms.sendSms("/api/code/send/register",val)});
					}
				}
				util.ajax({config:config});
		
		        }
		    })
		    //用户密码字段去除头尾空格验证字段长度
		    $("#userPwd").change(function(){
		        var rpwd = regitser.replacePasswoldNbsp($(this).val());
		        $(this).val(rpwd);
		        var thisval = $(this).val();
		        if(thisval.length < 6){
		            var obj = {
		                elm : $("#userPwd"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"用户密码不能少于6位");
		            return false;
		        }else{
		            var obj = {
		                elm : $("#userPwd"),
		                type : 1,
		                errortip :3
		            }
		            tips.checkFormTips(obj,"");
		        }
		    })
		    //确认密码去除头尾两端空格
		    $("#afterPwd").blur(function(){
		        var rpwd = regitser.replacePasswoldNbsp($(this).val());
		        $(this).val(rpwd);
		    })
		    
		    /* 点击提交按钮验证表单 */
		   	$(document).keydown(function(event){ 	
				if(event.keyCode==13){ 
					$("#submit").click();
				}
			}); 
		    $("#submit").click(function(){
		        var userPwd = $("#userPwd").val(),
		            userPhonetellogin = $("#userPhonetellogin").val(),
		            smsCodeNumber = $("#smsCodeNumber").val(),
		            afterPwd = $("#afterPwd").val()
		        if(userPhonetellogin == ""){
		            var obj = {
		                elm : $("#userPhonetellogin"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"用户手机不能为空");
		            return false;
		        }else if(!(/^1[3|4|5|7|8][0-9]\d{4,8}$/.test(userPhonetellogin))) {
		            var obj = {
		                elm : $("#userPhonetellogin"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"手机号码格式不正确");
		            return false;
		        }else if(smsCodeNumber == ""){
		            var obj = {
		                elm : $("#smsCodeNumber"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"验证码错误");
		            return false;
		        }else if(!(/^\d{4}$/.test(smsCodeNumber))){
		        	var obj = {
		                elm : $("#smsCodeNumber"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"验证码错误");
		            return false;
		        }else if(userPwd == ""){
		            var obj = {
		                elm : $("#userPwd"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"用户密码不能为空");
		            return false;
		        }else if($("#userPwd").val().length < 6){
		            var obj = {
		                elm : $("#userPwd"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"用户密码不能少于6位");
		            return false;
		        }else if(afterPwd != userPwd){
		        	$("#afterPwd").focus();
		            var obj = {
		                elm : $("#afterPwd"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"两次密码不一致");
		            return false;
		        }else if(afterPwd != ""){
		            var obj = {
		                elm : $("#afterPwd"),
		                type : 1,
		                errortip :3
		            }
		            tips.checkFormTips(obj,"");
		            regitser.userRegister();
		        }
		
		    })
    	},
    	
    	//去除头尾空格
    	replacePasswoldNbsp:function(str){
        	return str.replace(/(^\s*)|(\s*$)/g, '');
    	},
    	
    	//用户注册数据提交
    	userRegister:function (){
			var config = {
				url : "/api/user/regist",
				data:$('#userRegister').serialize(),
				success:function(data){
					regitser.registerCallback(data);
				}
			}
			util.ajax({config:config});
	    },
	    
    	/*
		 * 用户注册
		 * */
	    registerCallback:function(data){
			var obj = {},
				urlValue = util.getUrlValue("url"),
				url = urlValue != null ? urlValue : cache.base_url+"/user/regresult";
			if(data.code == 3001){
				obj = {
					elm : $("#userPhonetellogin"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"手机号已注册");
				return false;
			}else if(data.code ==3002){
				obj = {
					elm : $("#userPhonetellogin"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"用户手机号为空");
				return false;
			}else if(data.code ==3003){
				obj = {
					elm : $("#userPwd"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"用户密码为空");
				return false;
			}else if(data.code ==3004){
				obj = {
					elm : $("#smsCodeNumber"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"验证码为空");
				return false;
			}else if(data.code ==3006){
				obj = {
					elm : $("#userPhonetellogin"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"用户注册失败");
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
			}else if(data.code ==2013){
				obj = {
					elm : $("#smsCodeNumber"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"注册验证码发送受限");
				return false;
			}else if (data.code ==3005){
				var
				username = $("#userPhonetellogin").val(),
				userpassword = $("#userPwd").val(),
				obj = {
					username : username,
					userpassword : userpassword,
					type : 2,
					func:function(){location.href = url;}
				}
				caslogin.casLogin(obj);
				
			}
		},

		/**
		 * 更新验证码
		 */
		chageCode:function(){
			$('#codeImage').attr('src',Context.PATH+'/validateCode?r='+Math.random());
		}
    	
    }
    
    regitser.init();

})