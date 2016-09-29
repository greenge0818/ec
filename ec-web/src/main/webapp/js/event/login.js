define(function(require,exports,module){
	var $ = require("jquery"),
		util = require("util"),
		caslogin = require("caslogin"),
		prcsteel=require("prcsteel"),
		userCallback = require("usercallback"),
		cache = require("cache"),
		cas = require("cas"),
		tips = require("checkformtips");
		window.cas = cas;
	var login = {
		
		init:function(){
			/* 获得焦点清空输入框 */
			$("#userPhonetellogin1").inputFocus();
			$("#userPhonetellogin").inputFocus();
			$("#userPwd").inputFocus();
			$("#userPwd").focus().blur();
			$("#smsCodeNumber").inputFocus();
	
			/* 验证表单 */ 
			$("#userLogin").verifyForm(2,blur);
			$("#userLogin2").verifyForm(2,blur);
	
			/*  Tab切换 */ 
			$(".login-tab li").click(function(i){
				var li = $(".login-tab li");
				li.removeClass("active");
				$(this).addClass("active");
				if(li.eq(0).hasClass("active")){
					$("#logining  .form-err").css("display","none");
					$("#logining").removeClass("hide");
					$("#telCheckLogin").addClass("hide");
					login.checkFormblur(1);
				}else{
					$("#telCheckLogin  .form-err").css("display","none");
					$("#logining").addClass("hide");
					$("#telCheckLogin").removeClass("hide");
					login.checkFormblur(2);
				}
			})
	
			/* 点击清空按钮清空表单内容 */ 
			$(".input-empty").click(function(){
				$(this).closest(".item-text").find("input").val("");
			})
			
			//默认执行第一个	
			login.checkFormblur(1); 
			
		    /* 提交表单 */
		   $(document).keydown(function(event){ 
		   	var li = $(".login-tab li"),
		   		isActive = li.eq(0).hasClass("active");
		   		
				if(event.keyCode==13 && isActive == true){ 
					$("#dftSubmit").click();
				}else if(event.keyCode==13 && isActive == false){
					$("#quickSubmit").click();
				}
			}); 
			$("#dftSubmit").click(function(){
				cache.targetBtn = $(this);
				login.submitForm(1);
			})
			$("#quickSubmit").click(function(){
				cache.targetBtn = $(this);
				login.submitForm(2);
			})
		},

		//验证手机格式与长度
		checkPhone:function(elmid){
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
			var obj = {
				elm : elmid,
				type : 1,
				errortip :2
			}
			tips.checkFormTips(obj,"");
			return true;
		},
		/* 验证手机号码是否存在 */
		 checkFormblur:function(type){
			var userphone;
			if(type == 1){
				userphone = $("#userPhonetellogin1");
			}else{
				userphone = $("#userPhonetellogin");
			}
			userphone.change(function(){
				var val = $(this).val(),isTrue = login.checkPhone(userphone);
	        	if(!isTrue){
	        		return;
	        	}
				login.loginCheck(val);

			})
		},
		
		//提交表单数据
		submitForm:function(elm){
			var userPwd = $("#userPwd").val(),
				formid = "",
				type = 0,
				smsCodeNumber = $("#smsCodeNumber").val(),ischecktrue,userPhonetellogin="",phoneEl,pwd;
			if(elm ==1){
				phoneEl =  $("#userPhonetellogin1");
				userPhonetellogin =phoneEl.val();
				formid = $("#userLogin");
				type = 1;
				ischecktrue = login.checkPhone($("#userPhonetellogin1"));
	        	if(!ischecktrue){
	        		return;
	        	}
				if(userPwd == ""){
					var obj = {
						elm : $("#userPwd"),
						type : 1,
						errortip :1
					}
					tips.checkFormTips(obj,"用户密码不能为空");
					return false;
				}else if(userPwd.length < 6){
					var obj = {
						elm : $("#userPwd"),
						type : 1,
						errortip :1
					}
					tips.checkFormTips(obj,"用户密码不能少于6位");
					return false;
				}
				pwd = userPwd;
			}else{
				phoneEl =  $("#userPhonetellogin");
				userPhonetellogin = phoneEl.val();
				formid = $("#userLogin2");
				type = 2;
				ischecktrue = login.checkPhone($("#userPhonetellogin"));
	        	if(!ischecktrue){
	        		return;
	        	}
				if(smsCodeNumber == ""){
					var obj = {
						elm : $("#smsCodeNumber"),
						type : 1,
						errortip :1
					}
					tips.checkFormTips(obj,"验证码错误");
					return false;
				}
				pwd = smsCodeNumber;
			}
			var code = login.remoteValid(userPhonetellogin);
			if(code == "3012"){
				var obj = {
					elm : phoneEl,
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"用户账号被锁定");
				phoneEl.focus();
				return false;
			}else if(code =="1002"){
				obj = {
					elm : phoneEl,
					type : 1,
					errortip :1,
					link : "<a href="+cache.base_url+"/user/login>注册</a>"
				}
				tips.checkFormTips(obj,"手机号码未注册");
				return false;
			}
			cas.setCallback(function(){
				urlValue = util.getUrlValue("url"),
				url = urlValue != null ? urlValue : cache.base_url+"/";
				location.href = url
			});
			cas.setWrongTip(function(){
//				var obj = {
//					elm : phoneEl,
//					type : 1,
//					errortip :1
//				}
//				tips.checkFormTips(obj,(elm == 1 ? ("用户名或密码错误") : ("用户名或者验证码错误")));
//				phoneEl.focus();
				phoneEl.closest("ul").find(".err-msg-span").html((elm == 1 ? ("用户名或密码错误") : ("用户名或者验证码错误")));
				$(cache.targetBtn).removeAttr("disabled");
				return false;
			});
			cas.casLogin(userPhonetellogin,pwd);
			//login.userLogin(type);
		},
		remoteValid : function(username){
	    	var rtn ="0000";
	    	var config = {
		        	type : "POST",
					url : "/api/passport/verify",
					data : {
		                username:username,
		                password:"*****"
		            },
		            async:false,
					dataType: "json",
		            success : function(res, status, xhr) {
		            	rtn = res.code;
		            }
				}
			util.ajax({config:config});
			return rtn;
	    },
		//登录验证
		loginCheck:function(phone){
			var config = {
				url : "/api/user/phone/check",
				data:{"phone":phone},
				success:function(data){
					userCallback.loginCheckCallback(data);
				}
			}
			util.ajax({config:config});
		},

		
		//登录
		userLogin:function(logintype){
			var username = "",
				userpassword = "",
				smsCodeNumber = $("#smsCodeNumber").val(),
				urlValue = util.getUrlValue("url"),
				url = urlValue != null ? urlValue : cache.base_url+"/";
			if(logintype == 1){
				username = $("#userPhonetellogin1").val();
				userpassword = $("#userPwd").val();
				var obj = {
					username : username,
					userpassword : userpassword,
					type : 1, //请求类型 1为登陆 2为注册
					logintab :1,
					func:function(){location.href = url}
				}
			}else{
				username = $("#userPhonetellogin").val();
				userpassword = smsCodeNumber;
				var obj = {
					username : username,
					userpassword : userpassword,
					type : 1, //请求类型 1为登陆 2为注册
					logintab :2, //登陆类型 1为普通密码登陆 2为验证码登陆
					func:function(){location.href = url}
				}
			}
			caslogin.casLogin(obj);
       }
	}
		
	login.init();
})