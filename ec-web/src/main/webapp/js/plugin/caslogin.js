define(function(require,exports,module){
	var $ = require("jquery"),
		util = require("util"),
		cache=require("cache"),
		tips = require("checkformtips");

	var casLogin = {
		/**
	      *@date:2016-05-20
	      *@Modify-lastdate :2016-06-02
	      *@author :Liweitao
	      *@describe：根据传过来的用户名和密码执行登录动作
	      *@param: obj.username 用户名
	      *@param: obj.userpassword 用户密码
	      *@param: obj.type 请求类型（1、登录 2、注册 3、弹框登录注册）
	      *@param: obj.logintab 登录方式（1、密码登录 2、验证码登录 ）
	      *@param: obj.func 登录成功回调函数
	      *@return：null
	      *
	      */
		userlogin : function(obj){
			subtype = obj.type;
			logintab = obj.logintab;
			var config = {
	        	type : "POST",
				url : "/api/passport/verify",
				data : {
	                username:obj.username,
	                password:obj.userpassword
	            },
				dataType: "json",
	            success : function(res, status, xhr) {
	            	if(subtype == "1"){	//静态页登陆
	            		casLogin.loginCallback(res,1,logintab,obj.func); //单纯登录动作
	            	}else if(subtype == "2"){ //静态页注册
	            		casLogin.registerSuccessAutoLogin(res,obj.func); //注册成功自动登录
	            	}else if(subtype == "3"){ //弹层注册
	            		casLogin.loginCallback(res,2,logintab,obj.func); //注册成功自动登录
	            	}
	            }
			}
		util.ajax({config:config});
	   	},
	   	
	    verify : function(url,func){
			$.ajax({
	            type : "GET",
	            url : url,
	            success : function(text, textStatus, xhr) {
	                casLogin.fetchAccount(func);
	                casLogin.loginAfter();
	            }
	        });
	    },
	    
	   	loginAfter :function(){
	        var config = {
				url : "/api/user/login/after",
				asnyc:false,
	            success : function(data) {
	            }
			}
			util.ajax({config:config});
	    },
	    
	    fetchAccount:function(func){
	        var config = {
	    		type : "GET",
				url : "/api/passport/verify",
	            dataType: "text",
	            success : function(res, status, xhr) {
	            	func();	
	            }
			}
			util.ajax({config:config});
	    },
	      	
    	//登陆验证函数
    	loginCallback : function(data,style,logintab,func){
			var obj,
				style = style == null ? 1 : style, //错误提示语提示方式 1、显示图标提示 2、单纯文字提示
				logintab = logintab ==null ? 1:logintab; //登录方式 1、普通密码登录 2、验证码登录
			if(data.code =="1001"){
				obj = {
					elm : $(".userPhonetellogin"),
					type : style,
					errortip :2
				}
				tips.checkFormTips(obj,"");
				return false;
			}else if(data.code =="1002"){
				obj = {
					elm : $(".userPhonetellogin"),
					type : style,
					errortip :1,
					link : "<a href="+cache.base_url+"/user/login>注册</a>"
				}
				tips.checkFormTips(obj,"手机号码未注册");

			}else if(data.code =="1003"){
				obj = {
					elm : $(".userPhonetellogin"),
					type : style,
					errortip :1
				}
				tips.checkFormTips(obj,"手机号码不正确");

			}else if(data.code =="1004"){
				obj = {
					elm : $(".userPhonetellogin"),
					type : style,
					errortip :1,
					link : "<a href="+cache.base_url+"/user/register>注册</a>"
				}
				tips.checkFormTips(obj,"手机号码未注册");

			}else if(data.code =="2002"){
				obj = {
					elm : $("#smsCodeNumber"),
					type : style,
					errortip :1
				}
				tips.checkFormTips(obj,"验证码已超时");
				return false;
			}else if(data.code =="2004"){
				obj = {
					elm : $("#smsCodeNumber"),
					type : style,
					errortip :1
				}
				tips.checkFormTips(obj,"验证码错误");
				return false;
			}else if(data.code =="2008"){
				obj = {
					elm : $("#smsCodeNumber"),
					type : style,
					errortip :1
				}
				tips.checkFormTips(obj,"请先发送验证码");
				return false;
			}else if(data.code =="5001"){
				obj = {
					elm : $(".userPhonetellogin"),
					type : style,
					errortip :2,
					link : ""
				}
				tips.checkFormTips(obj,"");
				obj = {
					elm : $("#userPwd"),
					type : style,
					errortip :1
				}
				tips.checkFormTips(obj,"登陆密码为空");
				return false;
			}else if(data.code =="5003"){
				obj = {
					elm : $("#userPwd"),
					type : style,
					errortip :1
				}
				tips.checkFormTips(obj,"登录失败");
				return false;
			}else if(data.code =="5004"){
				if(logintab == "1"){
					obj = {
						elm : $("#userPwd"),
						type : style,
						errortip :1
					}
					tips.checkFormTips(obj,"验证码错误");
				}else{
					obj = {
						elm : $("#smsCodeNumber"),
						type : style,
						errortip :1
					}
					tips.checkFormTips(obj,"验证码错误");
				}
				return false;
			}else if(data.code =="3012"){
				obj = {
					elm : $(".userPhonetellogin"),
					type : style,
					errortip :1
				}
				tips.checkFormTips(obj,"账号被锁定");
			}
			else if(data.code =="5002"){
				casLogin.verify(data.data,func); //返回5002执行成功
			}else{
				obj = {
					elm : $(".userPhonetellogin"),
					type : style,
					errortip :1
				}
				tips.checkFormTips(obj,"授权失败");
			}
		},
		
		//注册成功自动登录函数
		registerSuccessAutoLogin:function(data,func){
			casLogin.verify(data.data,func);
		},

	}
	
	module.exports = {
		casLogin: casLogin.userlogin
	}
		

})