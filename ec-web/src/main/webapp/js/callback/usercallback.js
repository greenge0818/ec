define(function(require,exports,module){
		var $ = require("jquery"),
		util = require("util"),
		sendsms = require("sendsms"),
		cache=require("cache"),
		tips = require("checkformtips");
		
		var callback = {
		/*
		*
		* 登录验证手机号码
		* */
		loginCheckCallback:function(data){
			var obj = {},userPhonetellogin = $("#userPhonetellogin").val();
			if(data.code ==1002){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :2
				}
				tips.checkFormTips(obj,"");
				if (!$(".sms_btn").hasClass("msgnum")) {
					$(".sms_btn").removeClass("msg");
				}

				/*  发送验证码 */
				sendsms.sendSms("/api/code/send/login",userPhonetellogin);
				return false;
			}else if(data.code ==1001){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :1,
					link : "<a href="+cache.base_url+"/user/register>注册</a>"
				}
				tips.checkFormTips(obj,"手机号码未注册");
				$(".sms_btn").addClass("msg");
				return false;
			}else if(data.code ==1003){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"手机号码不正确");

			}else if(data.code ==1004){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :1,
					link : "<a href="+cache.base_url+"/user/register>注册</a>"
				}
				tips.checkFormTips(obj,"手机号码未注册");
				$(".sms_btn").addClass("msg");
				return false;
			}else if(data.code =="3012"){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"账号被锁定");
			}
		},
		
		/*
		*
		* 登录验证手机号码
		* */
		
		resetCheckCallback:function(data,fun){
			var obj = {},userPhonetellogin = $("#userPhonetellogin").val();
			if(data.code ==1002){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :2
				}
				tips.checkFormTips(obj,"");
				if (!$(".sms_btn").hasClass("msgnum")) {
					$(".sms_btn").removeClass("msg");
				}
			}else if(data.code ==1001){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :1,
					link : "<a href="+cache.base_url+"/user/register>注册</a>"
				}
				tips.checkFormTips(obj,"手机号码未注册");
				$(".sms_btn").addClass("msg");
				return false;
			}else if(data.code ==1003){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"手机号码不正确");
				return false;
			}else if(data.code ==1004){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :1,
					link : "<a href="+cache.base_url+"/user/register>注册</a>"
				}
				tips.checkFormTips(obj,"手机号码未注册");
				$(".sms_btn").addClass("msg");
				return false;
			}else if(data.code =="3012"){
				obj = {
					elm : $(".userPhonetellogin"),
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"账号被锁定");
				return false;
			}
			fun();
		},
		/*
		* 校验手机号码是否存在或者已注册
		* */
		checkUserRegCallback:function(data,fun){
			var obj = {},userPhonetellogin = $("#userPhonetellogin").val();
			if(data.code ==1002 || data.code ==3012){
				obj = {
					elm : $("#userPhonetellogin"),
					type : 1,
					errortip :1,
					link : "<a target='_blank'  href="+$("#loginUrl").attr("href")+">登录</a>"
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
			fun();
		}
		
		
		}
	module.exports = {
		loginCheckCallback: callback.loginCheckCallback,
		checkUserRegCallback:callback.checkUserRegCallback,
		registerCallback: callback.registerCallback,
		resetCheckCallback: callback.resetCheckCallback
	}
				
		
})